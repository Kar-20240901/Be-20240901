package com.kar20240901.be.base.web.model.enums.file;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.file.IBaseFileStorageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 存放文件的服务器类型：枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseFileStorageTypeEnum implements IBaseFileStorageType {

    EMPTY(-1), // 空

    ALI_YUN(101), // aliyun

    MINIO(201), // minio

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
