package com.kar20240901.be.base.web.listener.socket;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketEventBO;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaHelper;
import com.kar20240901.be.base.web.util.socket.WebSocketUtil;
import java.util.List;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * webSocket事件的 kafka监听器
 */
@Component
@KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{kafkaDynamicGroupIdConfiguration.getGroupId()}",
    batch = "true")
public class BaseWebSocketEventKafkaListener {

    public static final List<String> TOPIC_LIST =
        CollUtil.newArrayList(BaseKafkaTopicEnum.BASE_WEB_SOCKET_EVENT_TOPIC.name());

    // 目的：Long 转 String，Enum 转 code
    private static ObjectMapper objectMapper;

    public BaseWebSocketEventKafkaListener(ObjectMapper objectMapper) {

        BaseWebSocketEventKafkaListener.objectMapper = objectMapper;

    }

    @KafkaHandler
    public void receive(List<String> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        MyTryUtil.tryCatch(() -> {

            if (TempKafkaHelper.notHandleKafkaTopicCheck(TOPIC_LIST)) {
                return;
            }

            if (CollUtil.isNotEmpty(recordList)) {

                MyThreadUtil.execute(() -> {

                    for (String item : recordList) {

                        MyTryUtil.tryCatch(() -> {

                            BaseWebSocketEventBO<?> baseWebSocketEventBO =
                                objectMapper.readValue(item, BaseWebSocketEventBO.class);

                            // 发送：webSocket消息
                            WebSocketUtil.send(baseWebSocketEventBO);

                        });

                    }

                });

            }

        });

    }

}