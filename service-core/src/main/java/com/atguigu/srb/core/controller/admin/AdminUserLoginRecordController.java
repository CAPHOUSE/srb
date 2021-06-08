package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.result.Result;
import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.atguigu.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "会员登录日志接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/core/userLoginRecord")
public class AdminUserLoginRecordController {

    @Autowired
    private UserLoginRecordService userLoginRecordService;


    @ApiOperation("获取会员登录日志列表")
    @GetMapping("/listTop50/{userId}")
    public Result listTop50( @ApiParam( "用户id") @PathVariable Long userId){
        List<UserLoginRecord> loginRecordList = userLoginRecordService.listTop50(userId);
        return Result.ok(loginRecordList);
    }
}

