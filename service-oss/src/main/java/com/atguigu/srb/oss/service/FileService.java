package com.atguigu.srb.oss.service;

import java.io.InputStream;

public interface FileService {

    /**
     5
     * 文件上传至阿里云
     6
     */
    String upload(InputStream inputStream, String module, String fileName);

    /**
     * 根据路径删除文件
     * @param url
     */
    void removeFile(String url);
}
