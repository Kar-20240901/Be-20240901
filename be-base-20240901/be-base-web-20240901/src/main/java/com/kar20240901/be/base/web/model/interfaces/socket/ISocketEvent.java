package com.kar20240901.be.base.web.model.interfaces.socket;

import com.kar20240901.be.base.web.model.bo.socket.SocketEventBO;

public interface ISocketEvent {

    /**
     * socket已连接之后的事件
     */
    default void onConnected(SocketEventBO socketEventBo) {
    }

    /**
     * socket已断开之后的事件
     */
    default void onDisconnected(SocketEventBO socketEventBo) {
    }

}
