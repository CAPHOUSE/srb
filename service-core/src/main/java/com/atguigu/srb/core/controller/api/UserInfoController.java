package com.atguigu.srb.core.controller.api;


import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "会员接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;


    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVO registerVO){

        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();

        String codeGen = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);

        //CODE_ERROR(-206, "验证码不正确"),
        Assert.equals(code, codeGen, ResultEnum.CODE_ERROR);

        //PASSWORD_NULL_ERROR(-204, "密码不能为空"),
        Assert.notEmpty(password, ResultEnum.PASSWORD_NULL_ERROR);

        //CODE_NULL_ERROR(-205, "验证码不能为空"),
        Assert.notEmpty(code, ResultEnum.CODE_NULL_ERROR);


//        注册
        userInfoService.register(registerVO);

        return Result.ok().message("注册成功!");
    }


    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO, HttpServletRequest request){

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        //MOBILE_NULL_ERROR(-202, "手机号码不能为空"),
        Assert.notEmpty(mobile, ResultEnum.MOBILE_NULL_ERROR);

        //PASSWORD_NULL_ERROR(-204, "密码不能为空"),
        Assert.notEmpty(password, ResultEnum.PASSWORD_NULL_ERROR);

        //获取IP
        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);

        return Result.ok(userInfoVO).message("登录成功");
    }
}

