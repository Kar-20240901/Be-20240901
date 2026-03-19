package com.kar20240901.be.base.web.listener.wallet;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.configuration.pay.IBasePayRefHandler;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.enums.kafka.BaseKafkaTopicEnum;
import com.kar20240901.be.base.web.util.kafka.TempKafkaHelper;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 支付订单回调通知的 kafka监听器
 */
@Component
public class BasePayTradeNotifyKafkaListener {

    public static final List<String> TOPIC_LIST =
        CollUtil.newArrayList(BaseKafkaTopicEnum.BASE_PAY_TRADE_NOTIFY_TOPIC.name());

    private static final Map<Integer, IBasePayRefHandler> BASE_PAY_REF_HANDLER_MAP = MapUtil.newHashMap();

    // 目的：Long 转 String，Enum 转 code
    @Resource
    ObjectMapper objectMapper;

    public BasePayTradeNotifyKafkaListener(
        @Autowired(required = false) @Nullable List<IBasePayRefHandler> iBasePayRefHandlerList) {

        if (CollUtil.isNotEmpty(iBasePayRefHandlerList)) {

            for (IBasePayRefHandler item : iBasePayRefHandlerList) {

                BASE_PAY_REF_HANDLER_MAP.put(item.getBasePayRefType().getCode(), item);

            }

        }

    }

    @SneakyThrows
    @KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{kafkaDynamicGroupIdConfiguration.getGroupId()}",
        batch = "true")
    public void receive(List<ConsumerRecord<String, String>> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        if (TempKafkaHelper.notHandleKafkaTopicCheck(TOPIC_LIST)) {
            return;
        }

        if (CollUtil.isEmpty(BASE_PAY_REF_HANDLER_MAP)) {
            return;
        }

        for (ConsumerRecord<String, String> item : recordList) {

            BasePayDO basePayDO = objectMapper.readValue(item.value(), BasePayDO.class);

            IBasePayRefHandler iBasePayRefHandler = BASE_PAY_REF_HANDLER_MAP.get(basePayDO.getRefType());

            if (iBasePayRefHandler != null) {

                // 处理：具体的业务
                iBasePayRefHandler.handle(basePayDO);

            }

        }

    }

}
