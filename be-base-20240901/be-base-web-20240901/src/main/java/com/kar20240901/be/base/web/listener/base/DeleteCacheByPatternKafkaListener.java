package com.kar20240901.be.base.web.listener.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 删除缓存的 kafka监听器
 */
@Component
@KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{__listener.GROUP_ID}", batch = "true")
@Slf4j
public class DeleteCacheByPatternKafkaListener {

    public static final List<String> TOPIC_LIST =
        CollUtil.newArrayList(BaseKafkaTopicEnum.DELETE_CACHE_BY_PATTERN_TOPIC.name());

    public static final String GROUP_ID = BaseKafkaTopicEnum.DELETE_CACHE_BY_PATTERN_TOPIC.name();

    @Resource
    RedissonClient redissonClient;

    @KafkaHandler
    public void receive(List<ConsumerRecord<String, String>> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        // 延迟执行
        MyThreadUtil.schedule(() -> {

            log.info("删除缓存-通配符的 kafka监听器：{}", recordList);

            RBatch rBatch = redissonClient.createBatch();

            for (ConsumerRecord<String, String> item : recordList) {

                List<String> redisKeyList = JSONUtil.toList(item.value(), String.class);

                for (String subItem : redisKeyList) {

                    rBatch.getKeys().deleteByPatternAsync(subItem);

                }

            }

            rBatch.execute();

        }, new Date(System.currentTimeMillis() + 300));

    }

}
