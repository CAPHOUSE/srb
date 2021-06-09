package com.atguigu.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.atguigu.common.result.Result;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import com.atguigu.srb.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "会员账号绑定")
@Slf4j
@RestController
@RequestMapping("/api/core/userBind")
public class UserBindController {

    @Autowired
    private UserBindService userBindService;


    @ApiOperation("账号绑定提交数据")
    @PostMapping("/auth/bind")
    public Result bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request){
//            从header中获取token，判断token的可用性
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);

//          根据userId做用户绑定
        String formStr = userBindService.commitBindUser(userBindVO,userId);
        return Result.ok(formStr);
    }


    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notifys(HttpServletRequest request){
        Map<String, Object> paramMap = RequestHelper.
                switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调：" + JSON.toJSONString(paramMap));

        //校验签名
        if (!RequestHelper.isSignEquals(paramMap)){
            log.error("用户账号绑定异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        //修改绑定状态
        userBindService.notifys(paramMap);

        return "success";
    }

}

