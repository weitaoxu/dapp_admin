package com.qkbus.tools.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.qkbus.config.FileProperties;
import com.qkbus.exception.BadRequestException;
import com.qkbus.tools.config.OssConfig;
import com.qkbus.tools.service.AliOssService;
import com.qkbus.utils.FileUtil;
import com.qkbus.utils.HelpUtils;
import com.qkbus.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Slf4j
public class AliOssServiceImpl implements AliOssService {

    private final FileProperties properties;

    @Override
    public String ossUploadImage(MultipartFile upfile) {
        // 文件大小验证
        FileUtil.checkSize(properties.getAvatarMaxSize(), upfile.getSize());
        OSS oss = new OSSClientBuilder().build(OssConfig.OSS_ENDPOINT, OssConfig.OSS_ACCESS_KEY_ID, OssConfig.OSS_ACCESS_KEY_SECRET);
        String fileName = upfile.getOriginalFilename();
        //防止重复
        fileName = OssConfig.randomFileName() + fileName;
        try {
            boolean image = HelpUtils.isImage(upfile.getInputStream());
            if (!image) {
                log.error("图片上传失败，请选择正确的图片" + fileName);
                throw new BadRequestException("图片上传失败，请选择正确的图片!");
            }
            byte[] bytes = upfile.getBytes();
            String contentType = upfile.getContentType();
            OssConfig.uploadAppByteOSS(oss, bytes, contentType, fileName, OssConfig.IMAGE_DIR);
        } catch (IOException e) {
            log.error("上传图片失败：" + e.getMessage());
            e.printStackTrace();
        }
        return OssConfig.OSS_HOST + OssConfig.IMAGE_DIR + fileName;
    }

    @Override
    public Map<String, String> ossUploadApp(MultipartFile upfile, String fileName) {
        fileName += upfile.getOriginalFilename();
        OSS oss = new OSSClientBuilder().build(OssConfig.OSS_ENDPOINT, OssConfig.OSS_ACCESS_KEY_ID, OssConfig.OSS_ACCESS_KEY_SECRET);
        try {
            if (StringUtils.isNoneEmpty(fileName)) {
                boolean app = HelpUtils.isApp(fileName);
                if (!app) {
                    throw new BadRequestException("请选择正确的上传文件");
                }
            }
            byte[] bytes = upfile.getBytes();
            String contentType = upfile.getContentType();
            OssConfig.uploadAppByteOSS(oss, bytes, contentType, fileName, OssConfig.APP_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", fileName);
        map.put("url", OssConfig.OSS_HOST + OssConfig.APP_DIR + fileName);
        return map;
    }

    @Override
    public Map<String, String> ossUpload(MultipartFile upfile, String fileName) {
        OSS oss = new OSSClientBuilder().build(OssConfig.OSS_ENDPOINT, OssConfig.OSS_ACCESS_KEY_ID, OssConfig.OSS_ACCESS_KEY_SECRET);
        fileName += upfile.getOriginalFilename();
        try {
            byte[] bytes = upfile.getBytes();
            String contentType = upfile.getContentType();
            OssConfig.uploadAppByteOSS(oss, bytes, contentType, fileName, OssConfig.IMAGE_DIR);
        } catch (IOException e) {
            log.error("上传文件失败：" + e.getMessage());
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", fileName);
        map.put("url", OssConfig.OSS_HOST + OssConfig.FILE_DIR + fileName);
        return map;
    }
}
