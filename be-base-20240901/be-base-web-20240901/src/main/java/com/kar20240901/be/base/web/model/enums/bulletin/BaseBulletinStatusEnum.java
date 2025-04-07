package com.kar20240901.be.base.web.model.enums.bulletin;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公告状态
 */
@AllArgsConstructor
@Getter
public enum BaseBulletinStatusEnum {

    DRAFT(101), // 草稿

    PUBLICITY(201), // 公示

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
