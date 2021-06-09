package com.atguigu.srb.oss.controller.api;


import com.atguigu.common.exception.GlobalRuntimeException;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/api/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;


    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(@ApiParam("文件") @RequestParam("file") MultipartFile file,
                         @ApiParam("模块") @RequestParam("module") String module){

        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, fileName);
            return Result.ok(url);
        } catch (IOException e) {
            throw new GlobalRuntimeException(ResultEnum.UPLOAD_ERROR,e);
        }
    }

    @ApiOperation("删除OSS文件")
    @DeleteMapping("/remove")
    public Result remove(@ApiParam("要删除的文件路径") @RequestParam("url") String url){
        fileService.removeFile(url);
        return Result.ok().message("删除成功");
    }
}
