package com.atguigu.srb.sms.controller.api;

import com.atguigu.common.exception.Assert;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.common.util.RandomUtils;
import com.atguigu.common.util.RegexValidateUtils;
import com.atguigu.srb.sms.client.CoreUserInfoClient;
import com.atguigu.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private CoreUserInfoClient coreUserInfoClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public Result send( @ApiParam(value = "手机号") @PathVariable String mobile){

        //MOBILE_NULL_ERROR(-202, "手机号不能为空"),
        Assert.notEmpty(mobile, ResultEnum.MOBILE_NULL_ERROR);

        //判断手机是否被注册
        boolean flag = coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(flag == false,ResultEnum.MOBILE_EXIST_ERROR);

        //生成验证码
        String code = RandomUtils.getFourBitRandom();


        smsService.sendEmail(mobile,code);

        redisTemplate.opsForValue().set("srb:sms:code:" + mobile,code,5, TimeUnit.MINUTES);

        return Result.ok().message("验证码发送成功");
    }
}
