package com.kar20240901.be.base.web.model.configuration.socket;

import com.kar20240901.be.base.web.server.NettyWebSocketServer;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class INettyWebSocketDisable implements ISocketDisable {

    @Override
    public void handle(Set<Long> socketIdSet) {

        if (socketIdSet.contains(NettyWebSocketServer.baseSocketServerId)) {

            // 关闭 webSocket
            NettyWebSocketServer.stop(true);

        }

    }

}