package com.kar20240901.be.base.web.listener.base;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240901.be.base.web.model.domain.kafka.TempKafkaUserInfoDO;
import com.kar20240901.be.base.web.model.enums.kafka.TempKafkaTopicEnum;
import com.kar20240901.be.base.web.service.base.TempKafkaUserInfoService;
import com.kar20240901.be.base.web.util.base.Ip2RegionUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 更新：用户最近活动信息
 */
@Component
@KafkaListener(topics = "#{__listener.TOPIC_LIST}", groupId = "#{__listener.GROUP_ID}", batch = "true")
public class BaseKafkaListener {

    public static final String GROUP_ID = TempKafkaTopicEnum.TEMP_UPDATE_USER_LAST_ACTIVE.name();

    public static final List<String> TOPIC_LIST = CollUtil.newArrayList(GROUP_ID);

    // 目的：Long 转 String，Enum 转 code
    private static ObjectMapper objectMapper;

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        BaseKafkaListener.objectMapper = objectMapper;
    }

    private static TempKafkaUserInfoService tempKafkaUserInfoService;

    @Resource
    public void setTempKafkaUserInfoService(TempKafkaUserInfoService tempKafkaUserInfoService) {
        BaseKafkaListener.tempKafkaUserInfoService = tempKafkaUserInfoService;
    }

    @SneakyThrows
    @KafkaHandler
    public void receive(List<String> recordList, Acknowledgment acknowledgment) {

        acknowledgment.acknowledge();

        List<TempKafkaUserInfoDO> updateList = new ArrayList<>();

        for (String item : recordList) {

            TempKafkaUserInfoDO tempKafkaUserInfoDO = objectMapper.readValue(item, TempKafkaUserInfoDO.class);

            tempKafkaUserInfoDO.setLastRegion(Ip2RegionUtil.getRegion(tempKafkaUserInfoDO.getLastIp()));

            updateList.add(tempKafkaUserInfoDO);

        }

        if (CollUtil.isNotEmpty(updateList)) {

            tempKafkaUserInfoService.updateBatchById(updateList);

        }

    }

}
