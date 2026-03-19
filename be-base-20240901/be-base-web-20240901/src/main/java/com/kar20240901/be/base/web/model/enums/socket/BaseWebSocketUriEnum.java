package com.kar20240901.be.base.web.model.enums.socket;

import com.kar20240901.be.base.web.model.interfaces.socket.IWebSocketUri;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseWebSocketUriEnum implements IWebSocketUri {

    BASE_SIGN_OUT("/base/sign/out"), // 退出登录

    BASE_PAY_CLOSE_MODAL("/base/pay/closeModal"), // 关闭支付弹窗

    // im相关 ↓

    BASE_IM_SESSION_CONTENT_SEND("/base/im/session/content/send"), // im-发送消息

    BASE_IM_SESSION_CONTENT_UPDATE_TARGET_INPUT_FLAG("/base/im/session/content/updateTargetInputFlag"), // im-修改为输入中

    BASE_IM_SESSION_REF_USER_JOIN_USER_ID_SET("/base/im/session/refUser/join/userIdSet"), // im-聊天加入新的用户

    // im相关 ↑

    BASE_SOCKET_REF_USER_CHANGE_CONSOLE_FLAG_BY_ID_SET("/base/socketRefUser/changeConsoleFlagByIdSet"), // 打开/关闭 控制台

    BASE_REFRESH_BULLETIN("/base/bulletin/refresh"), // 刷新公告

    // 实时房间 ↓

    BASE_LIVE_ROOM_NEW_DATA("/base/live/room/newData"), // 实时房间有新的数据

    BASE_LIVE_ROOM_JOIN_ON_OTHER_DEVICE("/base/live/room/joinOnOtherDevice"), // 实时房间-您已经在其他设备上加入此房间

    BASE_LIVE_ROOM_NEW_USER("/base/live/room/newUser"), // 实时房间-有新的用户加入房间

    BASE_LIVE_ROOM_REMOVE_USER("/base/live/room/removeUser"), // 实时房间-有用户退出房间

    // 实时房间 ↑

    ;

    private final String uri;

}
