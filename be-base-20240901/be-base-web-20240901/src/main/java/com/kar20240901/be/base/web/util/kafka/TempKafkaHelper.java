package com.kar20240901.be.base.web.util.kafka;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.configuration.log.LogFilter;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Kafka 帮助类
 */
@Component
public class TempKafkaHelper {

    @Resource
    BaseConfiguration baseConfiguration;

    /**
     * 根据配置和开发环境，判断是否处理该主题
     *
     * @return true 不处理该主题 false 要处理该主题
     */
    public static boolean notHandleKafkaTopicCheck(List<String> topicList) {

        if (BaseConfiguration.prodFlag()) {

            if (CollUtil.containsAny(LogFilter.baseProperties.getProdNotHandleKafkaTopicSet(), topicList)) {

                return true;

            }

        } else {

            if (CollUtil.containsAny(LogFilter.baseProperties.getDevNotHandleKafkaTopicSet(), topicList)) {

                return true;

            }

        }

        return false;

    }

}
