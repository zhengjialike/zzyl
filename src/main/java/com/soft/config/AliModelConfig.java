package com.soft.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliModelConfig {

    // 从 application.properties 或 application.yml 中读取 api-key
    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean
    public DashScopeApi dashScopeApi() {
        // 返回阿里大模型访问对象，封装了 apiKey
        return DashScopeApi.builder().apiKey(apiKey).build();
    }
}