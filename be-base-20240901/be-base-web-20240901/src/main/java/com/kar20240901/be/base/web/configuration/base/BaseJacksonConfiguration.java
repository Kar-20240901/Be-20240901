package com.kar20240901.be.base.web.configuration.base;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseJacksonConfiguration {

    /**
     * jackson全局转化 Long类型为 String，解决 jackson序列化时传入前端 Long类型缺失精度问题
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(Long.class,
            ToStringSerializer.instance);

    }

}
