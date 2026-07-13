package com.zzyl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = {"com.zzyl", "com.soft"},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX,
            pattern = "com\\.soft\\.config\\.(MyMetaObjectHandler|MyBatisPlusConfig|MybatisplusPageConfig|CrossOriginConfig)"
        ),
        @ComponentScan.Filter(type = FilterType.REGEX,
            pattern = "com\\.soft\\.ZzylApplication"
        )
    }
)
@MapperScan({"com.zzyl.mapper", "com.soft.mapper"})
public class ZzylApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZzylApplication.class, args);
    }
}
