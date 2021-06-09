package com.atguigu.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.atguigu.common.exception.GlobalRuntimeException;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "数据字典管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/dict")
public class AdminDictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("数据的批量导入")
    @PostMapping("/import")
    public Result importData(@ApiParam("Excel数据字典文件") @RequestParam("file") MultipartFile file){
        try {
            dictService.importData(file.getInputStream());
            return Result.ok();
        } catch (Exception e) {
            throw new GlobalRuntimeException(ResultEnum.UPLOAD_ERROR,e);
        }
    }

    @ApiOperation("Excel数据的导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("数据字典").doWrite(dictService.listDictData());
        } catch (IOException e) {
            //EXPORT_DATA_ERROR(104, "数据导出失败"),
            throw  new GlobalRuntimeException(ResultEnum.EXPORT_DATA_ERROR, e);
        }
    }


    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public Result listByParentId(@ApiParam("上级节点id") @PathVariable Long parentId){
      List<Dict> dictList = dictService.listByParentId(parentId);
      return Result.ok(dictList);
    }
}

