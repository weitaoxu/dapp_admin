package com.qkbus.tools.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public interface AliOssService {


    /*
     * 上传图片
     * */
    String ossUploadImage(MultipartFile upfile);


    /*
     * 上传APP
     * */
    Map<String, String> ossUploadApp(MultipartFile upfile, String fileName);


    /*
     * 上传所有文件
     * */
    Map<String, String> ossUpload(MultipartFile upfile, String fileName);

}
