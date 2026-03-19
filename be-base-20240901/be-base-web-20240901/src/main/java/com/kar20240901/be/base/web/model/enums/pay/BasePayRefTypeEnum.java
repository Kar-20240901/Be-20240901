package com.kar20240901.be.base.web.model.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 关联的类型枚举类
 */
@AllArgsConstructor
@Getter
public enum BasePayRefTypeEnum implements IBasePayRefType {

    NONE(101), // 无

    WALLET_RECHARGE_USER(201), // 钱包充值-用户

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
