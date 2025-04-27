package com.kar20240901.be.base.web.model.enums.im;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImSessionContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话内容类型，枚举类
 */
@Getter
@AllArgsConstructor
public enum BaseImSessionContentTypeEnum implements IBaseImSessionContentType {

    TEXT(101), // 文字

    IMAGE(201), // 图片

    FILE(301), // 文件

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
