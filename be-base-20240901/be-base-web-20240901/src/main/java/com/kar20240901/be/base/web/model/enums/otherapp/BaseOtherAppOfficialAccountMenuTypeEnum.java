package com.kar20240901.be.base.web.model.enums.otherapp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.otherapp.IBaseOtherAppOfficialAccountMenuType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 第三方应用，公众号类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseOtherAppOfficialAccountMenuTypeEnum implements IBaseOtherAppOfficialAccountMenuType {

    WX_OFFICIAL_ACCOUNT(101), // 微信公众号

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
