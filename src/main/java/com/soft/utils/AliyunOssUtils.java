package com.soft.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class AliyunOssUtils {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.region}")
    private String region;

    /**
     * 将字节数组上传到阿里云OSS，并返回访问URL
     * @param objectName 对象完整路径（例如 "folder/image.jpg"）
     * @param content    文件字节内容
     * @return 上传后的文件访问URL，失败返回null
     */
    public String uploadFile(String objectName, byte[] content) {
        OSS ossClient = null;
        try {
            // 从环境变量获取访问凭证（需设置 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET）
            EnvironmentVariableCredentialsProvider credentialsProvider =
                    CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

            // 配置签名版本（V4）
            ClientBuilderConfiguration config = new ClientBuilderConfiguration();
            config.setSignatureVersion(SignVersion.V4);

            // 创建OSS客户端
            ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    //.credentialsProvider(new DefaultCredentialProvider(accessKeyId, accessKeySecret))
                    .clientConfiguration(config)
                    .region(region)
                    .build();

            // 上传文件
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));

            // 构建访问URL：格式 https://bucketName.endpoint/objectName
            // 注意：endpoint通常为 "https://oss-cn-chengdu.aliyuncs.com" 或 "oss-cn-chengdu.aliyuncs.com"
            String url;
            if (endpoint.startsWith("https://") || endpoint.startsWith("http://")) {
                String[] parts = endpoint.split("://");
                url = parts[0] + "://" + bucketName + "." + parts[1] + "/" + objectName;
            } else {
                // 若endpoint没有协议，默认添加https
                url = "https://" + bucketName + "." + endpoint + "/" + objectName;
            }
            return url;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}