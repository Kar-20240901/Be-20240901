package com.kar20240901.be.base.web.listener.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.configuration.base.IBaseDeleteLocalCache;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 删除缓存的 kafka监听器
 */
@Component
@KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{kafkaDynamicGroupIdConfiguration.getGroupId()}",
    batch = "true")
@Slf4j
public class DeleteLocalCacheKafkaListener {

    public static final List<String> TOPIC_LIST =
        CollUtil.newArrayList(BaseKafkaTopicEnum.DELETE_LOCAL_CACHE_TOPIC.name());

    public static final String GROUP_ID = BaseKafkaTopicEnum.DELETE_LOCAL_CACHE_TOPIC.name();

    private static final Map<Integer, IBaseDeleteLocalCache> BASE_DELETE_LOCAL_CACHE_MAP = MapUtil.newHashMap();

    // 目的：Long 转 String，Enum 转 code
    @Resource
    ObjectMapper objectMapper;

    public DeleteLocalCacheKafkaListener(
        @Autowired(required = false) @Nullable List<IBaseDeleteLocalCache> iBaseDeleteLocalCacheList) {

        if (CollUtil.isNotEmpty(iBaseDeleteLocalCacheList)) {

            for (IBaseDeleteLocalCache item : iBaseDeleteLocalCacheList) {

                BASE_DELETE_LOCAL_CACHE_MAP.put(item.getType().getCode(), item);

            }

        }

    }

    @SneakyThrows
    @KafkaHandler
    public void receive(List<ConsumerRecord<String, String>> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        for (ConsumerRecord<String, String> item : recordList) {

            BaseDeleteLocalCacheBO baseDeleteLocalCacheBO =
                objectMapper.readValue(item.value(), BaseDeleteLocalCacheBO.class);

            IBaseDeleteLocalCache iBaseDeleteLocalCache =
                BASE_DELETE_LOCAL_CACHE_MAP.get(baseDeleteLocalCacheBO.getType().getCode());

            if (iBaseDeleteLocalCache == null) {
                continue;
            }

            iBaseDeleteLocalCache.handle(baseDeleteLocalCacheBO);

        }

    }

}
