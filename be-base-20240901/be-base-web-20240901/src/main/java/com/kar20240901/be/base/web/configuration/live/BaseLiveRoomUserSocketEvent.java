package com.kar20240901.be.base.web.configuration.live;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.SocketEventBO;
import com.kar20240901.be.base.web.model.constant.socket.SocketAttributeKey;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.interfaces.socket.ISocketEvent;
import io.netty.channel.Channel;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BaseLiveRoomUserSocketEvent implements ISocketEvent {

    @Resource
    BaseLiveRoomUserMapper baseLiveRoomUserMapper;

    // 大key：房间id，小key：用户id，value：socketRefUserId
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<Long, Long>> LIVE_ROOM_MAP =
        new ConcurrentHashMap<>();

    @NotNull
    public static ConcurrentHashMap<Long, Long> getByRoomId(Long roomId) {

        return LIVE_ROOM_MAP.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>());

    }

    @Override
    public void onDisconnected(SocketEventBO socketEventBo) {

        Long userId = socketEventBo.getUserId();

        Long socketRefUserId = socketEventBo.getSocketRefUserId();

        Channel channel = socketEventBo.getChannel();

        Long liveRoomId = channel.attr(SocketAttributeKey.LIVE_ROOM_ID_KEY).get();

        ConcurrentHashMap<Long, Long> roomUserMap = getByRoomId(liveRoomId);

        roomUserMap.remove(userId, socketRefUserId);

        ChainWrappers.lambdaUpdateChain(baseLiveRoomUserMapper).eq(BaseLiveRoomUserDO::getRoomId, liveRoomId)
            .eq(BaseLiveRoomUserDO::getUserId, userId).eq(BaseLiveRoomUserDO::getSocketRefUserId, socketRefUserId)
            .remove();

    }

}
