package com.kar20240901.be.base.web.model.enums.im;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImSessionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话类型，枚举类
 */
@Getter
@AllArgsConstructor
public enum BaseImSessionTypeEnum implements IBaseImSessionType {

    PRIVATE_CHAT(101), // 私聊

    GROUP_CHAT(201), // 群聊

    CUSTOMER(301), // 客服

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
