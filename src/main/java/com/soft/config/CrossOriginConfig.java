package com.soft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossOriginConfig implements WebMvcConfigurer {

    @Value("${upload.local-dir:D:/zzyl-uploads}")
    private String localDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 把 /uploads/** 映射到本地上传目录
        String location = localDir.endsWith("/") ? localDir : localDir + "/";
        if (!location.startsWith("file:")) {
            location = "file:///" + location.replace("\\", "/");
        }
        registry.addResourceHandler("/uploads/**")
.addResourceLocations(location);
    }
}