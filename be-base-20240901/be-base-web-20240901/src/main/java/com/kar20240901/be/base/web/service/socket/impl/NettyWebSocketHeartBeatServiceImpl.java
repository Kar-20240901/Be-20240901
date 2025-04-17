package com.kar20240901.be.base.web.service.socket.impl;

import com.kar20240901.be.base.web.service.socket.NettyWebSocketHeartBeatService;
import com.kar20240901.be.base.web.socket.NettyWebSocketServer;
import org.springframework.stereotype.Service;

@Service
public class NettyWebSocketHeartBeatServiceImpl implements NettyWebSocketHeartBeatService {

    /**
     * 心跳检测
     */
    @Override
    public Long heartBeat() {
        return NettyWebSocketServer.baseSocketServerId;
    }

}
