package com.atguigu.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.srb.oss.service.FileService;
import com.atguigu.srb.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String upload(InputStream inputStream, String module, String fileName) {

//        构建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(
        OssProperties.ENDPOINT,OssProperties.KEY_ID,OssProperties.KEY_SECRET);

//        判断oss的BUCKET_NAME是否存在
        if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)){
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }

//        文件目录结构
        String date = new DateTime().toString("/yyyy/MM/dd/");
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String key = module + date + fileName;

//        上传文件流
        ossClient.putObject(OssProperties.BUCKET_NAME,key,inputStream);

//        关闭ossClient
        ossClient.shutdown();

//        文件的url
        String url = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;
        return url;
    }


    @Override
    public void removeFile(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(
        OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);

        // 删除文件
        String host = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/";
        String objectName = url.substring(host.length());

        ossClient.deleteObject(OssProperties.BUCKET_NAME,objectName);

//        关闭ossClient
        ossClient.shutdown();
    }
}
