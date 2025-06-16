package com.kar20240901.be.base.web.service.socket;

import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;

public interface NettyWebSocketHeartBeatService {

    Long heartBeat(ChannelDataBO channelDataBO);

}
