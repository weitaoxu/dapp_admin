package com.qkbus.tools.rest;

import com.qkbus.tools.config.OssConfig;
import com.qkbus.tools.service.AliOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oss")
@Api(tags = "工具：OSS")
public class OssController {


    @Autowired
    private AliOssService aliOssService;


    @ApiOperation("上传图片")
    @PostMapping("/ossUploadImage")
    @PreAuthorize("@el.check('admin','oss:image','Material:add','Notice:add')")
    public ResponseEntity<Object> ossUploadImage(@RequestParam(value = "upfile", required = false) MultipartFile upfile) {
        Map map = new HashMap<>();
        map.put("url", aliOssService.ossUploadImage(upfile));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @ApiOperation("上传APP")
    @PostMapping("/ossUploadApp")
    @PreAuthorize("@el.check('admin','oss:app','AppVersion:add')")
    public ResponseEntity<Object> ossUploadApp(@RequestParam(value = "upfile", required = false) MultipartFile upfile, String fileName) {
        OssConfig.randomFileName();
        return new ResponseEntity<>(aliOssService.ossUploadApp(upfile, ""), HttpStatus.OK);
    }

    @ApiOperation("上传所有文件")
    @PostMapping("/ossUpload")
    @PreAuthorize("@el.check('admin','oss:upload')")
    public ResponseEntity<Object> ossUpload(@RequestParam(value = "upfile", required = false) MultipartFile upfile) {
        return new ResponseEntity<>(aliOssService.ossUpload(upfile, OssConfig.randomFileName()), HttpStatus.OK);
    }

}
