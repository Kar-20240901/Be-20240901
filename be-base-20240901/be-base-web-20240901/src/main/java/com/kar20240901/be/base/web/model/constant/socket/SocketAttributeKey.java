package com.kar20240901.be.base.web.model.constant.socket;

import com.kar20240901.be.base.web.model.enums.base.BaseRequestCategoryEnum;
import io.netty.util.AttributeKey;
import java.util.Date;

public interface SocketAttributeKey {

    // 实时房间参数 ↓
    AttributeKey<Long> LIVE_ROOM_ID_KEY = AttributeKey.valueOf("LIVE_ROOM_ID_KEY"); // 实时房间主键id key
    // 实时房间参数 ↑

    // socket基础参数 ↓
    AttributeKey<Long> USER_ID_KEY = AttributeKey.valueOf("USER_ID_KEY"); // UserId key

    AttributeKey<Long> BASE_SOCKET_REF_USER_ID_KEY = AttributeKey.valueOf("BASE_SOCKET_REF_USER_ID_KEY");
    // BaseSocketRefUserId key

    AttributeKey<BaseRequestCategoryEnum> BASE_REQUEST_CATEGORY_ENUM_KEY =
        AttributeKey.valueOf("BASE_REQUEST_CATEGORY_ENUM_KEY"); // BaseRequestCategoryEnum key

    AttributeKey<String> IP_KEY = AttributeKey.valueOf("IP_KEY"); // Ip key

    AttributeKey<Date> ACTIVITY_TIME_KEY = AttributeKey.valueOf("ACTIVITY_TIME_KEY"); // 最近活跃时间 key
    // socket基础参数 ↑

}
