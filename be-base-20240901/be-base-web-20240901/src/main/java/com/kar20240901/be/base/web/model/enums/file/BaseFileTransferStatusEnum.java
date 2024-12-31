package com.kar20240901.be.base.web.model.enums.file;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件传输：状态，枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseFileTransferStatusEnum {

    TRANSFER_IN(101), // 传输中

    TRANSFER_STOP(201), // 传输暂停

    TRANSFER_COMPLETE(301), // 传输完成

    TRANSFER_CANCEL(401), // 传输取消

    COMPOSE_IN(501), // 合并中

    COMPOSE_COMPLETE(601), // 合并完成

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

}
