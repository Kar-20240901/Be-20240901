package com.kar20240901.be.base.web.model.enums.kafka;

import com.kar20240901.be.base.web.model.interfaces.IKafkaTopic;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * kafka 主题枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseKafkaTopicEnum implements IKafkaTopic {

    // 第三方应用相关 ↓

    BASE_OTHER_APP_WX_WORK_RECEIVE_MESSAGE_TOPIC, // 企业微信接收到消息之后，发送需要处理的对象 topic

    BASE_OTHER_APP_WX_OFFICIAL_ACCOUNT_RECEIVE_MESSAGE_TOPIC, // 微信公众号接收到消息之后，发送需要处理的对象 topic

    // 第三方应用相关 ↑

    // 缓存相关 ↓

    // 缓存相关 ↑

    // 支付相关 ↓

    BASE_PAY_TRADE_NOTIFY_TOPIC, // 支付订单回调通知时的 topic

    BASE_PAY_CLOSE_MODAL_TOPIC, // 关闭前端支付弹窗的 topic

    // 支付相关 ↑

    // socket相关 ↓

    SOCKET_DISABLE_TOPIC, // socket禁用的 topic，即：会断开所有的 socket连接

    SOCKET_ENABLE_TOPIC, // socket启用的 topic

    BASE_WEB_SOCKET_EVENT_TOPIC, // webSocket事件的 topic

    // socket相关 ↑

    ;

}
