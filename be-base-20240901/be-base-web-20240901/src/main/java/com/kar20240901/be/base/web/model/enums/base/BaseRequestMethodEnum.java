package com.kar20240901.be.base.web.model.enums.base;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@Getter
@Schema(description = "请求方式")
public enum BaseRequestMethodEnum {

    GET("GET"), //

    POST("POST"), //

    PUT("PUT"), //

    DELETE("DELETE"), //

    HEAD("HEAD"), //

    OPTIONS("OPTIONS"), //

    TRACE("TRACE"), //

    CONNECT("CONNECT"), //

    PATCH("PATCH"), //

    WEB_SOCKET("WEB_SOCKET") //

    ;

    @EnumValue
    @JsonValue
    private final String code;

    private static final Map<String, BaseRequestMethodEnum> MAP = new HashMap<>(BaseRequestMethodEnum.values().length);

    static {

        for (BaseRequestMethodEnum item : BaseRequestMethodEnum.values()) {

            MAP.put(item.getCode(), item);

        }

    }

    @NotNull
    public static BaseRequestMethodEnum getByCode(@Nullable String code) {

        if (code == null) {
            return POST;
        }

        return MAP.getOrDefault(code.toUpperCase(), POST);

    }

}
