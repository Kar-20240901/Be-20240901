package com.kar20240901.be.base.web.configuration.mybatisplus;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MybatisPlusConfiguration {

    /**
     * MybatisPlus插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        // 分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        return mybatisPlusInterceptor;

    }

    /**
     * 自定义：id生成器
     */
    @Bean
    @Primary
    public IdentifierGenerator idGenerator() {

        return entity -> IdGeneratorUtil.nextId();

    }

}
