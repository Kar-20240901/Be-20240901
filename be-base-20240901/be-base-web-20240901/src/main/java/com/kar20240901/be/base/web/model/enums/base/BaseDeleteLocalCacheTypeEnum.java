package com.kar20240901.be.base.web.model.enums.base;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseDeleteLocalCacheType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除本地缓存枚举类
 */
@AllArgsConstructor
@Getter
public enum BaseDeleteLocalCacheTypeEnum implements IBaseDeleteLocalCacheType {

    DELETE_FILE_SYSTEM_CLIENT_CACHE(101), // 删除文件系统客户端缓存

    DELETE_PAY_CLIENT_CACHE(201), // 删除支付客户端缓存

    ;

    @EnumValue
    @JsonValue
    private final int code; // 编码

}
