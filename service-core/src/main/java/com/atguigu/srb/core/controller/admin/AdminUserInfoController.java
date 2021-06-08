package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.result.Result;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.query.UserInfoQuery;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "会员管理")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {


    @Autowired
    private UserInfoService userInfoService;


   @ApiOperation("获取会员分页列表")
   @GetMapping("/list/{page}/{limit}")
   public Result listPage(@ApiParam("当前页") @PathVariable Long page,@ApiParam("每页记录数")
                          @PathVariable Long limit, UserInfoQuery userInfoQuery){
       Page<UserInfo> pageParam = new Page<>(page, limit);
       IPage<UserInfo> pageModel = userInfoService.listPage(pageParam, userInfoQuery);
       return Result.ok(pageModel).message("操作成功!");
   }


   @ApiOperation("锁定和解锁")
   @PutMapping("/lock/{id}/{status}")
   public Result lock(@ApiParam("用户id") @PathVariable Long id,
                      @ApiParam("用户状态") @PathVariable Integer status){
        userInfoService.lock(id,status);
        return Result.ok();
   }
}

