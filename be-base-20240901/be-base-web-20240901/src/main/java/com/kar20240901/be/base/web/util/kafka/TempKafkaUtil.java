package com.kar20240901.be.base.web.util.kafka;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketByteEventBO;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketStrEventBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.model.interfaces.kafka.IKafkaTopic;
import com.kar20240901.be.base.web.util.socket.WebSocketUtil;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka 工具类
 */
@Component
public class TempKafkaUtil {

    private static KafkaTemplate<String, String> stringKafkaTemplate;

    private static KafkaTemplate<String, byte[]> byteArrayKafkaTemplate;

    // 目的：Long 转 String，Enum 转 code
    private static ObjectMapper objectMapper;

    public TempKafkaUtil(KafkaTemplate<String, String> stringKafkaTemplate, ObjectMapper objectMapper,
        KafkaTemplate<String, byte[]> byteArrayKafkaTemplate) {

        TempKafkaUtil.stringKafkaTemplate = stringKafkaTemplate;
        TempKafkaUtil.objectMapper = objectMapper;
        TempKafkaUtil.byteArrayKafkaTemplate = byteArrayKafkaTemplate;

    }

    /**
     * 发送消息，备注：建议封装一层
     */
    @SneakyThrows
    public static void sendStr(IKafkaTopic iKafkaTopic, Object data) {

        if (data instanceof String) {

            stringKafkaTemplate.send(iKafkaTopic.name(), (String)data);

        } else {

            stringKafkaTemplate.send(iKafkaTopic.name(), objectMapper.writeValueAsString(data));

        }

    }

    /**
     * 发送消息，备注：建议封装一层
     */
    @SneakyThrows
    public static void sendByte(IKafkaTopic iKafkaTopic, byte[] byteArr) {

        byteArrayKafkaTemplate.send(iKafkaTopic.name(), byteArr);

    }

    /**
     * 发送消息：socket禁用的 topic
     */
    public static void sendSocketDisableTopic(Set<Long> socketIdSet) {

        sendStr(BaseKafkaTopicEnum.SOCKET_DISABLE_TOPIC, socketIdSet);

    }

    /**
     * 发送消息：socket启用的 topic
     */
    public static void sendSocketEnableTopic(Set<Long> socketIdSet) {

        sendStr(BaseKafkaTopicEnum.SOCKET_ENABLE_TOPIC, socketIdSet);

    }

    /**
     * 发送消息：webSocket字符串事件的 topic
     */
    public static void sendBaseWebSocketStrEventTopic(BaseWebSocketStrEventBO<?> baseWebSocketEventBO) {

        sendStr(BaseKafkaTopicEnum.BASE_WEB_SOCKET_STR_EVENT_TOPIC, baseWebSocketEventBO);

    }

    /**
     * 发送消息：webSocket字节事件的 topic
     *
     * @param byteArr 传输的二进制数据，备注：会把 baseWebSocketByteEventBO转换为 json字符串，然后把该 json字符串的长度，拼接到一个新数组的最前面，然后再拼接
     *                json字符串的内容，然后再拼接 byteArr的内容，进行传输
     */
    public static void sendBaseWebSocketByteEventTopic(BaseWebSocketByteEventBO<?> baseWebSocketByteEventBO,
        byte[] byteArr) {

        String jsonStr = JSONUtil.toJsonStr(baseWebSocketByteEventBO);

        // 获取：需要发送的二进制数据
        byte[] sendByteArr = WebSocketUtil.getSendByteArr(jsonStr, byteArr);

        sendByte(BaseKafkaTopicEnum.BASE_WEB_SOCKET_BYTE_EVENT_TOPIC, sendByteArr);

    }

    /**
     * 发送消息：删除缓存-通配符的 topic
     */
    public static void sendDeleteCacheByPatternTopic(List<String> patternList) {

        sendStr(BaseKafkaTopicEnum.DELETE_CACHE_BY_PATTERN_TOPIC, patternList);

    }

    /**
     * 发送消息：删除缓存的 topic
     */
    public static void sendDeleteCacheTopic(List<String> keyList) {

        sendStr(BaseKafkaTopicEnum.DELETE_CACHE_TOPIC, keyList);

    }

    /**
     * 发送消息：删除本地缓存的 topic
     */
    public static void sendDeleteLocalCacheTopic(BaseDeleteLocalCacheBO baseDeleteLocalCacheBO) {

        sendStr(BaseKafkaTopicEnum.DELETE_LOCAL_CACHE_TOPIC, baseDeleteLocalCacheBO);

    }

    /**
     * 发送消息：支付状态发生改变时的 topic
     */
    public static void sendPayStatusChangeTopic(BasePayDO basePayDO) {

        sendStr(BaseKafkaTopicEnum.BASE_PAY_TRADE_NOTIFY_TOPIC, basePayDO);

    }

}
