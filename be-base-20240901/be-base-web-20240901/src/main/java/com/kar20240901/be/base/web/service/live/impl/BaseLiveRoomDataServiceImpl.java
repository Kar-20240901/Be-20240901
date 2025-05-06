package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketEventBO;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDataDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomDataAddDataDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomDataService;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomDataServiceImpl extends ServiceImpl<BaseLiveRoomDataMapper, BaseLiveRoomDataDO>
    implements BaseLiveRoomDataService {

    @Resource
    BaseLiveRoomUserMapper baseLiveRoomUserMapper;

    /**
     * 新增数据
     */
    @Override
    public String addData(BaseLiveRoomDataAddDataDTO dto, ChannelDataBO channelDataBO) {

        long currentTimeMillis = System.currentTimeMillis();

        if (dto.getCreateTs() > currentTimeMillis) {

            R.error("操作失败：数据不合法",
                StrUtil.format("当前时间：{}，传递时间：{}", currentTimeMillis, dto.getCreateTs()));

        }

        List<BaseLiveRoomUserDO> baseLiveRoomUserDoList =
            ChainWrappers.lambdaQueryChain(baseLiveRoomUserMapper).eq(BaseLiveRoomUserDO::getRoomId, dto.getRoomId())
                .select(BaseLiveRoomUserDO::getSocketRefUserId, BaseLiveRoomUserDO::getUserId).list();

        Map<Long, Long> socketMap = baseLiveRoomUserDoList.stream()
            .collect(Collectors.toMap(BaseLiveRoomUserDO::getUserId, BaseLiveRoomUserDO::getSocketRefUserId));

        if (!socketMap.containsValue(channelDataBO.getSocketRefUserId())) {

            R.error("操作失败：您不在房间内",
                StrUtil.format("roomId：{}，socketRefUserId：{}", dto.getRoomId(), channelDataBO.getSocketRefUserId()));

        }

        // 发送：webSocket数据
        sendWebSocket(dto, socketMap);

        BaseLiveRoomDataDO baseLiveRoomDataDO = new BaseLiveRoomDataDO();

        baseLiveRoomDataDO.setRoomId(dto.getRoomId());
        baseLiveRoomDataDO.setCreateTs(dto.getCreateTs());
        baseLiveRoomDataDO.setData(dto.getData());
        baseLiveRoomDataDO.setCreateId(channelDataBO.getUserId());
        baseLiveRoomDataDO.setMediaType(dto.getMediaType());

        save(baseLiveRoomDataDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 发送：webSocket数据
     */
    private static void sendWebSocket(BaseLiveRoomDataAddDataDTO dto, Map<Long, Long> socketMap) {

        BaseWebSocketEventBO<BaseLiveRoomDataAddDataDTO> baseWebSocketEventBO = new BaseWebSocketEventBO<>();

        baseWebSocketEventBO.setUserIdSet(socketMap.keySet());

        baseWebSocketEventBO.setBaseSocketRefUserIdSet(CollUtil.newHashSet(socketMap.values()));

        WebSocketMessageDTO<BaseLiveRoomDataAddDataDTO> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_LIVE_ROOM_NEW_DATA, dto);

        baseWebSocketEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送：webSocket事件
        TempKafkaUtil.sendBaseWebSocketEventTopic(baseWebSocketEventBO);

    }

}
