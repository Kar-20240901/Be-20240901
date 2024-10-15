package com.kar20240901.be.base.web.model.enums;

import com.kar20240901.be.base.web.model.interfaces.IKafkaTopic;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * kafka 主题枚举类
 */
@AllArgsConstructor
@Getter
public enum TempKafkaTopicEnum implements IKafkaTopic {

    // 缓存相关 ↓

    // 缓存相关 ↑

    // 用户相关 ↓

    TEMP_UPDATE_USER_LAST_ACTIVE // 更新：用户最近活动信息

    // 用户相关 ↑

    ;

}
