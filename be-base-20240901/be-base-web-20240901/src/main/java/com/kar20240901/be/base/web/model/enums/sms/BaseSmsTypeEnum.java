package com.kar20240901.be.base.web.model.enums.sms;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.sms.IBaseSmsType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信方式类型：枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseSmsTypeEnum implements IBaseSmsType {

    ALI_YUN(101), // 阿里云

    TENCENT_YUN(201), // 腾讯云

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
