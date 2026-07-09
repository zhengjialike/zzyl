package com.soft.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisplusPageConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 创建分页拦截器
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();

        // 设置数据库类型（MySQL、Oracle、PostgreSQL等）
        paginationInterceptor.setDbType(DbType.MYSQL);

        // 设置请求页大于最大页后的操作，true回到首页，false继续请求（默认false）
        paginationInterceptor.setOverflow(true);

        // 设置单页最大限制数量，默认无限制
        paginationInterceptor.setMaxLimit(500L);

        // 添加分页插件（如果配置多个插件，分页插件必须最后添加）
        interceptor.addInnerInterceptor(paginationInterceptor);

        return interceptor;
    }
}