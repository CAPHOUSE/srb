package com.atguigu.srb.core.controller.api;


import com.atguigu.common.result.Result;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.service.BorrowInfoService;
import com.atguigu.srb.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "借款信息")
@Slf4j
@RestController
@RequestMapping("/api/core/borrowInfo")
public class BorrowInfoController {

    @Autowired
    private BorrowInfoService borrowInfoService;

    @ApiOperation("获取借款额度")
    @GetMapping("/auth/getBorrowAmount")
    public Result getBorrowAmount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        BigDecimal borrowAmount  = borrowInfoService.getBorrowAmount(userId);
        return Result.ok(borrowAmount);
    }


    @ApiOperation("提交借款申请")
    @GetMapping("/auth/save")
    public Result save(@RequestBody BorrowInfo borrowInfo,HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        borrowInfoService.saveBorrowInfo(borrowInfo, userId);
        return Result.ok().message("提交成功");
    }
}

