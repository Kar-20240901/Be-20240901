package com.kar20240901.be.base.web.controller.base.websocket;

import com.kar20240901.be.base.web.model.annotation.base.NettyWebSocketController;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.socket.SocketHeartBeatVO;
import com.kar20240901.be.base.web.service.socket.NettyWebSocketHeartBeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@NettyWebSocketController
@RequestMapping(value = "/netty/webSocket/heartBeat")
@Tag(name = "websocket-基础-心跳检测")
public class NettyWebSocketHeartBeatController {

    @Resource
    NettyWebSocketHeartBeatService baseService;

    @Operation(summary = "心跳检测")
    @PostMapping
    public R<SocketHeartBeatVO> heartBeat(ChannelDataBO channelDataBO) {
        return R.okData(baseService.heartBeat(channelDataBO));
    }

}
