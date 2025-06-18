package com.kar20240901.be.base.web.service.socket;

import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.vo.socket.SocketHeartBeatVO;

public interface NettyWebSocketHeartBeatService {

    SocketHeartBeatVO heartBeat(ChannelDataBO channelDataBO);

}
