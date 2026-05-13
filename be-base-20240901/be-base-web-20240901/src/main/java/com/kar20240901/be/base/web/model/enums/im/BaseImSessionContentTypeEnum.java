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

    NONE(-1), // 无

    // > 100 && < 1000 会统计未读数量 ↓

    TEXT(101), // 文字

    TEXT_FRIEND_APPLY_FINISH(102), // 添加好友成功

    TEXT_GROUP_CREATE_FINISH(103), // 创建群组成功

    TEXT_GROUP_APPLY_FINISH(104), // 加入群组成功

    IMAGE(201), // 图片

    FILE(301), // 文件

    // > 100 && < 1000 会统计未读数量 ↑

    ;

    @EnumValue
    @JsonValue
    private final int code; // 类型编码

    public static final Map<Integer, IBaseImSessionContentType> MAP = new HashMap<>();

    static {

        for (IBaseImSessionContentType item : values()) {

            if (item.getCode() == NONE.getCode()) {
                continue;
            }

            MAP.put(item.getCode(), item);

        }

    }

}
