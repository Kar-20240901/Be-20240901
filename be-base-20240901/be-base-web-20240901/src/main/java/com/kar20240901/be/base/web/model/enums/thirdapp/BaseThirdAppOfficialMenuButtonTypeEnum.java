package com.kar20240901.be.base.web.model.enums.thirdapp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方应用，公众号按钮类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseThirdAppOfficialMenuButtonTypeEnum {

    CLICK(101, "click"), // 点击

    VIEW(201, "view"), // 链接

    MINIPROGRAM(301, "miniprogram"), // 小程序

    ;

    @EnumValue
    @JsonValue
    private final int code;

    private final String name;

}
