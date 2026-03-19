package com.kar20240901.be.base.web.model.enums.socket;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * socket类型
 */
@AllArgsConstructor
@Getter
public enum BaseSocketTypeEnum {

    TCP_PROTOBUF(101), //

    WEB_SOCKET(201), //

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
