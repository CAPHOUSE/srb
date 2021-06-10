package com.atguigu.srb.core.controller.admin;

import com.atguigu.common.result.Result;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.BorrowerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "借款人管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/borrower")
public class AdminBorrowerController {

    @Autowired
    private BorrowerService borrowerService;


    @ApiOperation("获取借款人分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(@ApiParam("当前页码") @PathVariable Long page,
                           @ApiParam("每页记录数") @PathVariable Long limit,
                           @ApiParam("分页关键字") @RequestParam String keyword){
        Page<Borrower> pageParam = new Page<>(page, limit);
        IPage<Borrower> pageModel = borrowerService.listPage(pageParam, keyword);
        return Result.ok(pageModel);
    }


    @ApiOperation("获取借款人信息")
    @GetMapping("/show/{id}")
    public Result show(@ApiParam("借款人id") @PathVariable Long id){
         BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(id);
         return Result.ok(borrowerDetailVO);
    }


    @ApiOperation("借款额度审批")
    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO){
        borrowerService.approval(borrowerApprovalVO);
        return Result.ok().message("审批完成");
    }
}
