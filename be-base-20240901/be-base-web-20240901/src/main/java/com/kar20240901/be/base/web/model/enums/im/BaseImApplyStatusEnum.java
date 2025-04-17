package com.kar20240901.be.base.web.model.enums.im;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 申请状态，枚举类
 */
@Getter
@AllArgsConstructor
public enum BaseImApplyStatusEnum {

    APPLYING(101), // 申请中

    PASSED(201), // 已通过

    REJECTED(301), // 已拒绝

    BLOCKED(401), // 已被拉黑，单独

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
