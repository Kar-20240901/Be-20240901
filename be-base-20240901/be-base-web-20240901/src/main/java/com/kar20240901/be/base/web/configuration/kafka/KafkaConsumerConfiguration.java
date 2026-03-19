package com.kar20240901.be.base.web.configuration.kafka;

import java.util.Map;
import javax.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class KafkaConsumerConfiguration {

    @Resource
    KafkaProperties kafkaProperties;

    @Resource
    ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer;

    @Primary
    @Bean
    public ConsumerFactory<Object, Object> stringConsumerFactory() {

        Map<String, Object> consumerPropertiesMap = kafkaProperties.buildConsumerProperties();

        consumerPropertiesMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumerPropertiesMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(consumerPropertiesMap);

    }

    @Bean
    public ConsumerFactory<Object, Object> byteArrayConsumerFactory() {

        Map<String, Object> consumerPropertiesMap = kafkaProperties.buildConsumerProperties();

        consumerPropertiesMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumerPropertiesMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(consumerPropertiesMap);

    }

    /**
     * 不要修改为：stringKafkaListenerContainerFactory，因为不然 kafka会默认注入一个对象
     */
    @Primary
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        kafkaListenerContainerFactoryConfigurer.configure(factory, stringConsumerFactory());

        return factory;

    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> byteArrayKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();

        kafkaListenerContainerFactoryConfigurer.configure(factory, byteArrayConsumerFactory());

        return factory;

    }

}
