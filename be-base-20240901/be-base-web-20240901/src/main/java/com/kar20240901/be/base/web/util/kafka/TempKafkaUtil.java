package com.kar20240901.be.base.web.util.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.domain.kafka.TempKafkaUserInfoDO;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.model.enums.kafka.TempKafkaTopicEnum;
import com.kar20240901.be.base.web.model.interfaces.kafka.IKafkaTopic;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka 工具类
 */
@Component
public class TempKafkaUtil {

    private static KafkaTemplate<String, String> kafkaTemplate;

    // 目的：Long 转 String，Enum 转 code
    private static ObjectMapper objectMapper;

    public TempKafkaUtil(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {

        TempKafkaUtil.kafkaTemplate = kafkaTemplate;
        TempKafkaUtil.objectMapper = objectMapper;

    }

    /**
     * 发送消息，备注：建议封装一层
     */
    @SneakyThrows
    public static void send(IKafkaTopic iKafkaTopic, Object data) {

        if (data instanceof String) {

            kafkaTemplate.send(iKafkaTopic.name(), (String)data);

        } else {

            kafkaTemplate.send(iKafkaTopic.name(), objectMapper.writeValueAsString(data));

        }

    }

    /**
     * 发送消息：更新：用户信息
     */
    public static void sendTempUpdateUserInfoTopic(TempKafkaUserInfoDO tempKafkaUserInfoDO) {

        send(TempKafkaTopicEnum.TEMP_UPDATE_USER_LAST_ACTIVE, tempKafkaUserInfoDO);

    }

    /**
     * 发送消息：socket禁用的 topic
     */
    public static void sendSocketDisableTopic(Set<Long> socketIdSet) {

        send(BaseKafkaTopicEnum.SOCKET_DISABLE_TOPIC, socketIdSet);

    }

    /**
     * 发送消息：socket启用的 topic
     */
    public static void sendSocketEnableTopic(Set<Long> socketIdSet) {

        send(BaseKafkaTopicEnum.SOCKET_ENABLE_TOPIC, socketIdSet);

    }

    /**
     * 发送消息：webSocket事件的 topic
     */
    public static void sendBaseWebSocketEventTopic(Object baseWebSocketEventBO) {

        send(BaseKafkaTopicEnum.BASE_WEB_SOCKET_EVENT_TOPIC, baseWebSocketEventBO);

    }

}
