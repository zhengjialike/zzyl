package com.soft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson 日期时间格式化配置
 * 统一处理 LocalDateTime 的序列化和反序列化
 */
@Configuration
public class JacksonConfig {

    /**
     * 定义日期时间格式：年-月-日 时:分:秒
     * 例如：2026-07-10 12:30:09
     */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 配置 ObjectMapper Bean
     * 用于统一处理 JSON 序列化和反序列化
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 创建 JavaTimeModule 模块，用于处理 Java 8 时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 注册 LocalDateTime 的序列化器（后端 -> 前端）
        javaTimeModule.addSerializer(LocalDateTime.class,
            new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

        // 注册 LocalDateTime 的反序列化器（前端 -> 后端）
        javaTimeModule.addDeserializer(LocalDateTime.class,
            new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

        // 将模块注册到 ObjectMapper
        objectMapper.registerModule(javaTimeModule);

        // 禁用将日期序列化为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }
}