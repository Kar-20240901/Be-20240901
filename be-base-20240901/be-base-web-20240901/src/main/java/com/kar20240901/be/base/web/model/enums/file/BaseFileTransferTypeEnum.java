package com.kar20240901.be.base.web.model.enums.file;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件传输：类型，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseFileTransferTypeEnum {

    UPLOAD(101), // 上传

    DOWNLOAD(201), // 下载

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
