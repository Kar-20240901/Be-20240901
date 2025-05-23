package com.kar20240901.be.base.web.listener.base;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 删除缓存的 kafka监听器
 */
@Component
@KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{__listener.GROUP_ID}", batch = "true")
@Slf4j
public class DeleteCacheKafkaListener {

    public static final List<String> TOPIC_LIST = CollUtil.newArrayList(BaseKafkaTopicEnum.DELETE_CACHE_TOPIC.name());

    public static final String GROUP_ID = BaseKafkaTopicEnum.DELETE_CACHE_TOPIC.name();

    @Resource
    RedissonClient redissonClient;

    @KafkaHandler
    public void receive(@Payload List<String> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        MyTryUtil.tryCatch(() -> {

            for (String item : recordList) {

                redissonClient.getKeys().deleteByPatternAsync(item);

            }

        });

    }

}
