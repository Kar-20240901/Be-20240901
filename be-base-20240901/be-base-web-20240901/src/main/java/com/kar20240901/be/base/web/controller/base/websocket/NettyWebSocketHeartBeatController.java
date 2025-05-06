package com.kar20240901.be.base.web.controller.base.websocket;

import com.kar20240901.be.base.web.model.annotation.base.NettyWebSocketController;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.socket.NettyWebSocketHeartBeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@NettyWebSocketController
@RequestMapping(value = "/netty/webSocket/heartBeat")
@Tag(name = "基础-WebSocket-心跳检测")
public class NettyWebSocketHeartBeatController {

    @Resource
    NettyWebSocketHeartBeatService baseService;

    @Operation(summary = "心跳检测")
    @PostMapping
    public R<Long> heartBeat() {
        return R.okData(baseService.heartBeat());
    }

}
