package com.kar20240901.be.base.web.configuration.redis;

import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ReadMode;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseRedissonConfigurationCustomizer {

    @Bean
    @ConditionalOnMissingBean(value = RedissonAutoConfigurationCustomizer.class)
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer() {

        return config -> {

            config.setCodec(new JsonJacksonCodec()); // 设置为：json序列化，目的：方便看

            if (config.isClusterConfig()) {

                config.useClusterServers().setReadMode(ReadMode.MASTER); // 默认为 SLAVE，会出现延迟的情况

            }

        };

    }

}
