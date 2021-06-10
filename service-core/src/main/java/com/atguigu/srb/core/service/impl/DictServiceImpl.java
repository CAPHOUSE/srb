package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Resource
    private RedisTemplate redisTemplate;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("导入成功!");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
//        将dict列表转化成excelDictDTO
        List<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict,excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        }
        return excelDictDTOList;
    }


    @Override
    public List<Dict> listByParentId(Long parentId) {

//        查询redis是否存在数据列表
        try {
            List<Dict> dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if (dictList != null){
                log.info("从redis中获取数据列表");
                return dictList;
            }
        } catch (Exception e) {
           log.info("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }

        log.info("从数据库中获取数据列表");
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",parentId);
        List<Dict> dictList = baseMapper.selectList(wrapper);
//        填充hasChildren
        for (Dict dict : dictList) {
//            判断当前是否有子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        }
        try {
//      将数据存入redis
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId,dictList,100, TimeUnit.DAYS);
        } catch (Exception e) {
            log.info("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }

        return dictList;
    }


    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return this.listByParentId(dict.getId());
    }

    /**
     * 判断当前id是否有子节点
     * @param id
     * @return
     */
    private boolean hasChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count.intValue() > 0){
            return true;
        }else {
            return false;
        }
    }

}
