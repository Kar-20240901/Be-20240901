package com.kar20240901.be.base.web.model.enums.socket;

import com.kar20240901.be.base.web.model.interfaces.socket.IWebSocketUri;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseWebSocketUriEnum implements IWebSocketUri {

    BASE_SIGN_OUT("/base/sign/out"), // 退出登录

    BASE_PAY_CLOSE_MODAL("/base/pay/closeModal"), // 关闭支付弹窗

    BASE_IM_SESSION_CONTENT_SEND("/base/im/session/content/send"), // im-发送消息

    BASE_IM_SESSION_REF_USER_JOIN_USER_ID_SET("/base/im/session/refUser/join/userIdSet"), // im-聊天加入新的用户

    BASE_SOCKET_REF_USER_CHANGE_CONSOLE_FLAG_BY_ID_SET("/base/socketRefUser/changeConsoleFlagByIdSet"), // 打开/关闭 控制台

    ;

    private final String uri;

}
