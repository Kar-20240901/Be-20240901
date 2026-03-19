package com.kar20240901.be.base.web.model.enums.thirdapp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.thirdapp.IBaseThirdAppType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 三方应用类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseThirdAppTypeEnum implements IBaseThirdAppType {

    WX_MINI_PROGRAM(101), // 微信小程序

    WX_OFFICIAL(102), // 微信公众号

    WX_WORK(103), // 企业微信

    ALI_PAY_PROGRAM(201), // 支付宝小程序

    BAI_DU(301), // 百度

    VOLC_ENGINE(401), // 火山引擎

    MICROSOFT(501), // 微软

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
