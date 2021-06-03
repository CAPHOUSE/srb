package com.atguigu.srb.core.controller.admin;


import com.atguigu.common.exception.GlobalRuntimeException;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "数据字典管理")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/core/dict")
public class AdminDictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("数据的批量导入")
    @PostMapping("/import")
    public Result importData(@ApiParam("Excel数据字典文件") @RequestParam("file") MultipartFile file){
        try {
            dictService.importData(file.getInputStream());
            return Result.ok();
        } catch (Exception e) {
            throw new GlobalRuntimeException(ResultEnum.UPLOAD_ERROR,e);
        }
    }
}

