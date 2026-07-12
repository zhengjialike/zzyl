package com.soft.controller;

import com.soft.utils.AliyunOssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    @Value("${aliyun.oss.enabled:false}")
    private boolean ossEnabled;

    @Value("${upload.local-dir:D:/zzyl-uploads}")
    private String localDir;

    @Value("${upload.url-prefix:http://localhost:8080/uploads}")
    private String urlPrefix;

    /**
     * 文件上传：oss.enabled=true 走阿里云OSS, 否则走本地存储
     */
    @RequestMapping("/upload")
    public String fileUpload(MultipartFile mf) {
        if (mf == null || mf.isEmpty()) {
            return "error: file is empty";
        }
        String oldName = mf.getOriginalFilename();
        String ext = oldName != null && oldName.contains(".") ? oldName.substring(oldName.lastIndexOf(".")) : "";
        String newName = UUID.randomUUID().toString() + ext;

        // 1. OSS 启用时走 OSS
        if (ossEnabled) {
            try {
                String path = aliyunOssUtils.uploadFile(newName, mf.getBytes());
                if (path != null) return path;
            } catch (Exception ex) {
                // fallthrough to local
            }
        }

        // 2. 本地存储
        try {
            Path dir = Paths.get(localDir);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            File dest = new File(localDir, newName);
            mf.transferTo(dest);
            return urlPrefix + "/" + newName;
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
}