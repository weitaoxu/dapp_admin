package com.qkbus.tools.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
//@PropertySource({"classpath.application.yml","classpath.application-prod.yml"})
public class OssConfig {

    /*
     * OSS_ENDPOINT
     * */
    public static String OSS_ENDPOINT;
    /*
     * OSS_ACCESS_KEY_ID
     * */
    public static String OSS_ACCESS_KEY_ID;
    /*
     *  OSS_ACCESS_KEY_SECRET
     * */
    public static String OSS_ACCESS_KEY_SECRET;
    /*
     * OSS_BUCKET
     * */
    public static String OSS_BUCKET;
    /*
     * OSS_HOST
     * */
    public static String OSS_HOST;

    /*
     * app上传文件夹
     * */
    public static String FILE_DIR = "file/";

    /*
     * app上传文件夹
     * */
    public static String APP_DIR = "app/";

    /*
     * image上传文件夹
     * */
    public static String IMAGE_DIR = "image/";

    @Value("${aliyun.oss.endpoint}")
    public void setOssEndpoint(String ossEndpoint) {
        OSS_ENDPOINT = ossEndpoint;
    }

    @Value("${aliyun.oss.access.key.id}")
    public void setOssAccessKeyId(String ossAccessKeyId) {
        OSS_ACCESS_KEY_ID = ossAccessKeyId;
    }

    @Value("${aliyun.oss.access.key.secret}")
    public void setOssAccessKeySecret(String ossAccessKeySecret) {
        OSS_ACCESS_KEY_SECRET = ossAccessKeySecret;
    }

    @Value("${aliyun.oss.bucket}")
    public void setOssBucket(String ossBucket) {
        OSS_BUCKET = ossBucket;
    }

    @Value("${aliyun.oss.host}")
    public void setOssHost(String ossHost) {
        OSS_HOST = ossHost;
    }


    //上传图片获取时间
    public static String getCurrTime() {
        SimpleDateFormat dfhs = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dfhs.format(date);
    }

    /**
     * @return 名称：yyyyMMddHHmmss + 时间戳 + 4为随机数
     */
    public static String randomFileName() {
        Random random = new Random();
        return getCurrTime() + System.currentTimeMillis() + random.nextInt(10000);
    }

    /**
     * @param ossClient   oss对象
     * @param b           文件字节数组
     * @param contentType 图片类型
     * @param fileName    图片名称：yyyyMMddHHmmss + 时间戳 + 4为随机数 + 图片原名称
     * @param typeDir     上传文件类型   直接就是存储路径
     * @return
     */
    public static String uploadAppByteOSS(OSS ossClient, byte[] b, String contentType, String fileName, String typeDir) {
        Long fileSize = (long) b.length;
        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(contentType);
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
        // http.//bigen.oss-cn-shanghai.aliyuncs.com/ueditor/upfile  实际上是这个路径
        ossClient.putObject(OssConfig.OSS_BUCKET, typeDir + fileName, new ByteArrayInputStream(b), metadata);
        ossClient.shutdown();
        String filepath = typeDir + fileName;
        System.out.println("oss路径：" + filepath);
        return filepath;
    }


}
