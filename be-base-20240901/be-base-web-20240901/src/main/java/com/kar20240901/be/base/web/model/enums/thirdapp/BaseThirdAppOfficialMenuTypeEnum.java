package com.kar20240901.be.base.web.model.enums.thirdapp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.thirdapp.IBaseThirdAppOfficialMenuType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 三方应用，公众号类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseThirdAppOfficialMenuTypeEnum implements IBaseThirdAppOfficialMenuType {

    WX_OFFICIAL(101), // 微信公众号

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
