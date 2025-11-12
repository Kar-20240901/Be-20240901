package com.kar20240901.be.base.web.model.enums.im;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImSessionContentType;
import java.util.HashMap;
import java.util.Map;
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

    public static final Map<Integer, IBaseImSessionContentType> MAP = new HashMap<>();

    static {

        for (IBaseImSessionContentType item : values()) {

            MAP.put(item.getCode(), item);

        }

    }

}
