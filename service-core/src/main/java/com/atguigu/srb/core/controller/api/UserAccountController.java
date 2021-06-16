package com.atguigu.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.atguigu.common.result.Result;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.service.UserAccountService;
import com.atguigu.srb.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */

@Api(tags = "会员账户")
@RestController
@RequestMapping("/api/core/userAccount")
@Slf4j
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;


    @ApiOperation("充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public Result commitCharge(@ApiParam("充值金额") @PathVariable BigDecimal chargeAmt,
                               HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return Result.ok(formStr);
    }



    @ApiOperation(value = "用户充值异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户充值异步回调：" + JSON.toJSONString(paramMap));
        //校验签名
        if(RequestHelper.isSignEquals(paramMap)) {
            //充值成功交易
            if("0001".equals(paramMap.get("resultCode"))) {
                return userAccountService.notifys(paramMap);
            } else {
                log.info("用户充值异步回调充值失败：" + JSON.toJSONString(paramMap));
                return "success";
            }
        } else {
            log.info("用户充值异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
    }

}

