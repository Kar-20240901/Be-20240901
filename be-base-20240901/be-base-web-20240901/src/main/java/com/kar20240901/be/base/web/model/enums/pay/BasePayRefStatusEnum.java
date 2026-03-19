package com.kar20240901.be.base.web.model.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关联的状态枚举类
 */
@AllArgsConstructor
@Getter
public enum BasePayRefStatusEnum implements IBasePayRefStatus {

    NONE(101), // 无

    WAIT_PAY(201), // 待付款

    PAID(301), // 已付款

    FINISHED(401), // 关联业务已处理

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
