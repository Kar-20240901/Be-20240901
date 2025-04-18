package com.kar20240901.be.base.web.model.enums.im;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聊天类型，枚举类
 */
@Getter
@AllArgsConstructor
public enum BaseImTypeEnum implements IBaseImType {

    FRIEND(101), // 好友

    GROUP(201), // 群组

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
