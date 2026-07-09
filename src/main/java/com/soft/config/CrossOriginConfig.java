package com.soft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                // 允许所有路径
                .allowedOriginPatterns("http://localhost")       // 允许所有来源（生产环境请替换为具体域名）
                .allowedMethods("*")              // 允许所有请求方法（GET, POST, PUT, DELETE等）
                .allowedHeaders("*")              // 允许所有请求头
                .allowCredentials(true)           // 允许携带凭证（Cookie）
                .maxAge(3600);                    // 预检请求缓存时间（秒）
    }
}