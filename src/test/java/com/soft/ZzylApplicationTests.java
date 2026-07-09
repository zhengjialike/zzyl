package com.soft;

import com.soft.utils.AliyunOssUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
class ZzylApplicationTests {

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    @Test
    void uploadFileUtil() {
        // 将本地需要上传的文件封装为对象
        File file = new File("C:/Users/12/Pictures/3015/zzyl.png");
        try {
            // 将本地文件对象转化为字节数组
            byte[] bytes = Files.readAllBytes(file.toPath());
            String url = aliyunOssUtils.uploadFile("4.png", bytes);
            System.out.println("url=" + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
