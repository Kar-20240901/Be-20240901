package com.kar20240901.be.base.web.configuration.kafka;

import java.util.Map;
import javax.annotation.Resource;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfiguration {

    @Resource
    KafkaProperties kafkaProperties;

    @Primary
    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {

        Map<String, Object> producerPropertiesMap = kafkaProperties.buildProducerProperties();

        producerPropertiesMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        producerPropertiesMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(producerPropertiesMap);

    }

    @Bean
    public ProducerFactory<String, byte[]> byteArrayProducerFactory() {

        Map<String, Object> producerPropertiesMap = kafkaProperties.buildProducerProperties();

        producerPropertiesMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        producerPropertiesMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);

        return new DefaultKafkaProducerFactory<>(producerPropertiesMap);

    }

    @Primary
    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {

        return new KafkaTemplate<>(stringProducerFactory());

    }

    @Bean
    public KafkaTemplate<String, byte[]> byteArrayKafkaTemplate() {

        return new KafkaTemplate<>(byteArrayProducerFactory());

    }

}
