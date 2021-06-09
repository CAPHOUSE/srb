package com.atguigu.srb.core.service.impl;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.core.enums.UserBindEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserBind;
import com.atguigu.srb.core.mapper.UserBindMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String commitBindUser(UserBindVO userBindVO, Long userId) {

//        判断身份证是否相同
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("id_card",userBindVO.getIdCard())
                .ne("user_id",userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        Assert.isNull(userBind, ResultEnum.USER_BIND_IDCARD_EXIST_ERROR);

//        用户是否曾经添加过绑定记录
        QueryWrapper<UserBind> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        userBind = baseMapper.selectOne(wrapper);

        if (userBind == null) {
//        创建用户绑定的记录
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVO, userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            baseMapper.insert(userBind);
        }else {
            BeanUtils.copyProperties(userBindVO,userBind);
            baseMapper.updateById(userBind);
        }

//        组装自动提交表单的数据
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard",userBindVO.getIdCard());
        paramMap.put("personalName", userBindVO.getName());
        paramMap.put("bankType", userBindVO.getBankType());
        paramMap.put("bankNo", userBindVO.getBankNo());
        paramMap.put("mobile", userBindVO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));



        String formStr = FormHelper.buildForm(HfbConst.USERBIND_URL,paramMap);
        return formStr;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifys(Map<String, Object> paramMap) {
        String bindCode = (String)paramMap.get("bindCode");
        //会员id
        String agentUserId = (String)paramMap.get("agentUserId");

        //根据user_id查询user_bind记录
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", agentUserId);

        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        baseMapper.updateById(userBind);

        //更新用户表
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }
}
