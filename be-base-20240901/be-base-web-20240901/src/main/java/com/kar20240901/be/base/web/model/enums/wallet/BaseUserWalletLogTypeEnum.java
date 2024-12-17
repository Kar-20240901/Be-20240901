package com.kar20240901.be.base.web.model.enums.wallet;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.wallet.IBaseUserWalletLogType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户钱包操作日志类型，枚举类
 */
@Getter
@AllArgsConstructor
public enum BaseUserWalletLogTypeEnum implements IBaseUserWalletLogType {

    ADD_PAY(101, "支付充值"), //

    ADD_BACKGROUND(102, "后台充值"), //

    ADD_TIME_CHECK(103, "超时返还"), //

    REDUCE_WITHDRAW(201, "用户提现"), //

    REDUCE_BACKGROUND(202, "后台扣除"), //

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

    private final String name;

}
