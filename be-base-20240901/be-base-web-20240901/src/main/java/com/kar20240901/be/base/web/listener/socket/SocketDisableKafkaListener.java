package com.kar20240901.be.base.web.listener.socket;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.configuration.socket.ISocketDisable;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.Nullable;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * socket禁用的 kafka监听器
 */
@Component
@KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{kafkaDynamicGroupIdConfiguration.getGroupId()}",
    batch = "true")
@Slf4j
public class SocketDisableKafkaListener {

    public static final List<String> TOPIC_LIST = CollUtil.newArrayList(BaseKafkaTopicEnum.SOCKET_DISABLE_TOPIC.name());

    @Nullable
    private static List<ISocketDisable> iSocketDisableList;

    @Resource
    public void setiSocketDisableList(List<ISocketDisable> iSocketDisableList) {
        SocketDisableKafkaListener.iSocketDisableList = iSocketDisableList;
    }

    @KafkaHandler
    public void receive(List<ConsumerRecord<String, String>> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        Set<Long> socketIdSet = recordList.stream() //
            .map(it -> JSONUtil.toList(it.value(), Long.class)) //
            .flatMap(Collection::stream) //
            .collect(Collectors.toSet());

        if (CollUtil.isEmpty(iSocketDisableList)) {
            return;
        }

        for (ISocketDisable item : iSocketDisableList) {

            // 执行：处理
            item.handle(socketIdSet);

        }

    }

}
