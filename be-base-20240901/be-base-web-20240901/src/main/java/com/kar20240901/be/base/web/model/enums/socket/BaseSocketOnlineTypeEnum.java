package com.kar20240901.be.base.web.model.enums.socket;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * socket 在线状态
 */
@AllArgsConstructor
@Getter
public enum BaseSocketOnlineTypeEnum {

    ONLINE(101), // 在线

    HIDDEN(201), // 隐身

    PING_TEST(100001), // ping测试，用于：获取延迟最低的 socket服务器

    ;

    @EnumValue
    @JsonValue
    private final int code;

    @NotNull
    public static BaseSocketOnlineTypeEnum getByCode(Integer code) {

        if (code == null) {
            return ONLINE;
        }

        for (BaseSocketOnlineTypeEnum item : BaseSocketOnlineTypeEnum.values()) {

            if (item.getCode() == code) {

                return item;

            }

        }

        return ONLINE;

    }

}
