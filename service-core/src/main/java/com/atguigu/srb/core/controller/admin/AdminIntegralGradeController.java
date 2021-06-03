package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.exception.Assert;
import com.atguigu.common.exception.GlobalRuntimeException;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "积分等级管理")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Autowired
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public Result listAll(){
        return Result.ok(integralGradeService.list());
    }


    @ApiOperation(value = "根据id删除数据记录",notes = "逻辑删除记录")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@ApiParam(value = "数据id",example = "100") @PathVariable Long id){
        boolean result = integralGradeService.removeById(id);
        if (result){
            return Result.ok();
        }else {
            throw new GlobalRuntimeException(ResultEnum.BAD_SQL_GRAMMAR_ERROR);
        }
    }

    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public Result save(@ApiParam("积分等级对象") @RequestBody IntegralGrade integralGrade){
        //如果借款额度为空就手动抛出一个自定义的异常！
        Assert.notNull(integralGrade.getBorrowAmount(),ResultEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean result = integralGradeService.save(integralGrade);
        if (result){
            return Result.ok();
        }else {
            throw new GlobalRuntimeException(ResultEnum.BAD_SQL_GRAMMAR_ERROR);
        }
    }


    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public Result getById(@ApiParam("数据id") @PathVariable Long id){
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (null != integralGrade){
            return Result.ok(integralGrade);
        }else {
            throw new GlobalRuntimeException(ResultEnum.BAD_SQL_GRAMMAR_ERROR);
        }
    }

    @ApiOperation("更新积分等级")
    @PutMapping("/update")
    public Result updateById(@ApiParam("积分等级对象") @RequestBody IntegralGrade integralGrade){
        boolean result = integralGradeService.updateById(integralGrade);
        if (result){
            return Result.ok();
        }else {
           throw new GlobalRuntimeException(ResultEnum.BAD_SQL_GRAMMAR_ERROR);
        }
    }
}

