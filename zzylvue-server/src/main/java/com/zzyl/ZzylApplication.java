package com.zzyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zzyl.mapper")
public class ZzylApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZzylApplication.class, args);
    }
}
