package com.kar20240901.be.base.web.listener.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 删除缓存的 kafka监听器
 */
@Component
public class DeleteCacheKafkaListener {

    public static final List<String> TOPIC_LIST = CollUtil.newArrayList(BaseKafkaTopicEnum.DELETE_CACHE_TOPIC.name());

    public static final String GROUP_ID = BaseKafkaTopicEnum.DELETE_CACHE_TOPIC.name();

    @Resource
    RedissonClient redissonClient;

    @KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{__listener.GROUP_ID}", batch = "true")
    public void receive(List<ConsumerRecord<String, String>> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        // 延迟执行
        MyThreadUtil.schedule(() -> {

            List<String> redisKeyList = new ArrayList<>(recordList.size());

            for (ConsumerRecord<String, String> item : recordList) {

                List<String> redisKeyListTemp = JSONUtil.toList(item.value(), String.class);

                redisKeyList.addAll(redisKeyListTemp);

            }

            redissonClient.getKeys().delete(redisKeyList.toArray(new String[0]));

        }, new Date(System.currentTimeMillis() + 300));

    }

}
