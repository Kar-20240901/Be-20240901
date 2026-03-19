package com.kar20240901.be.base.web.model.enums.file;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件上传：文件类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseFileTypeEnum {

    FILE(101), // 文件

    FOLDER(201), // 文件夹

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
