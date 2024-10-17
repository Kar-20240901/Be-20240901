package com.kar20240901.be.base.web.configuration.kafka;

import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KafkaDynamicGroupIdConfiguration {

    @Value("${server.port:8080}")
    private int port;

    @PostConstruct
    public void postConstruct() {

        log.info("kafka 动态 groupId：{}", getGroupId());

    }

    public String getGroupId() {

        return BaseConfiguration.MAC_ADDRESS + ":" + port;

    }

}
