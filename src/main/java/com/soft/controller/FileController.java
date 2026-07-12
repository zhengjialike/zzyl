package com.soft.controller;

import com.soft.utils.AliyunOssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileController {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    /**
     * 处理文件上传请求，返回上传后的OSS访问路径
     */
    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("mf") MultipartFile mf) {
        try {
            // 获取原始文件名
            String oldName = mf.getOriginalFilename();
            // 提取扩展名
            String ext = oldName.substring(oldName.lastIndexOf("."));
            // 生成新文件名（UUID + 扩展名）
            String newName = UUID.randomUUID().toString() + ext;
            // 获取文件字节数组
            byte[] bytes = mf.getBytes();
            // 调用工具类上传至OSS
            String path = aliyunOssUtils.uploadFile(newName, bytes);
            return path;
        } catch (Exception ex) {
            ex.printStackTrace();
            // 上传失败时返回错误信息（可根据需求调整）
            return "error";
        }
    }
}