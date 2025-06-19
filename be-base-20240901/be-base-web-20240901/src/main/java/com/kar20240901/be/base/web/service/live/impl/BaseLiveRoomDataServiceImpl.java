package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.configuration.live.BaseLiveRoomUserSocketEvent;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketByteEventBO;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDataDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomDataAddDataDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomDataService;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomDataServiceImpl extends ServiceImpl<BaseLiveRoomDataMapper, BaseLiveRoomDataDO>
    implements BaseLiveRoomDataService {

    @Resource
    BaseLiveRoomUserMapper baseLiveRoomUserMapper;

    CopyOnWriteArrayList<BaseLiveRoomDataDO> BASE_LIVE_ROOM_DATA_DO_LIST = new CopyOnWriteArrayList<>();

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 2000)
    public void scheduledSava() {

        CopyOnWriteArrayList<BaseLiveRoomDataDO> tempBaseLiveRoomDataDoList;

        synchronized (BASE_LIVE_ROOM_DATA_DO_LIST) {

            if (CollUtil.isEmpty(BASE_LIVE_ROOM_DATA_DO_LIST)) {
                return;
            }

            tempBaseLiveRoomDataDoList = BASE_LIVE_ROOM_DATA_DO_LIST;
            BASE_LIVE_ROOM_DATA_DO_LIST = new CopyOnWriteArrayList<>();

        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            // 批量保存数据
            saveBatch(tempBaseLiveRoomDataDoList);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 新增数据
     */
    @Override
    public String addData(BaseLiveRoomDataAddDataDTO dto, ChannelDataBO channelDataBO) {

        long currentTimeMillis = System.currentTimeMillis();

        if (dto.getCreateTs() > currentTimeMillis + 10 * 1000) {

            R.error("操作失败：数据不合法",
                StrUtil.format("当前时间：{}，传递时间：{}", currentTimeMillis, dto.getCreateTs()));

        }

        ConcurrentHashMap<Long, Long> roomUserMap = BaseLiveRoomUserSocketEvent.getByRoomId(dto.getRoomId());

        if (!roomUserMap.containsValue(channelDataBO.getSocketRefUserId())) {

            R.error("操作失败：您不在房间内",
                StrUtil.format("roomId：{}，socketRefUserId：{}", dto.getRoomId(), channelDataBO.getSocketRefUserId()));

        }

        ConcurrentHashMap<Long, Long> socketMap = new ConcurrentHashMap<>(roomUserMap);

        socketMap.remove(channelDataBO.getUserId());

        dto.setUserId(channelDataBO.getUserId());

        dto.setSocketRefUserId(channelDataBO.getSocketRefUserId());

        // 发送：webSocket数据
        sendWebSocket(dto, socketMap, channelDataBO.getByteArr());

        BaseLiveRoomDataDO baseLiveRoomDataDO = new BaseLiveRoomDataDO();

        baseLiveRoomDataDO.setRoomId(dto.getRoomId());
        baseLiveRoomDataDO.setCreateTs(dto.getCreateTs());
        baseLiveRoomDataDO.setData(channelDataBO.getByteArr());
        baseLiveRoomDataDO.setCreateId(channelDataBO.getUserId());
        baseLiveRoomDataDO.setMediaType(dto.getMediaType());

        boolean firstBlobFlag = BooleanUtil.isTrue(dto.getFirstBlobFlag());

        baseLiveRoomDataDO.setFirstBlobFlag(firstBlobFlag);

        //        BASE_LIVE_ROOM_DATA_DO_LIST.add(baseLiveRoomDataDO);

        if (firstBlobFlag) {

            String firstBlobStr = Base64.encode(channelDataBO.getByteArr());

            if (StrUtil.isNotBlank(firstBlobStr)) {

                // 更新第一个 blob的值
                ChainWrappers.lambdaUpdateChain(baseLiveRoomUserMapper)
                    .eq(BaseLiveRoomUserDO::getRoomId, dto.getRoomId())
                    .eq(BaseLiveRoomUserDO::getUserId, channelDataBO.getUserId())
                    .eq(BaseLiveRoomUserDO::getSocketRefUserId, channelDataBO.getSocketRefUserId())
                    .set(BaseLiveRoomUserDO::getFirstBlobStr, firstBlobStr).update();

            }

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 发送：webSocket数据
     */
    private static void sendWebSocket(BaseLiveRoomDataAddDataDTO dto, Map<Long, Long> socketMap, byte[] byteArr) {

        if (socketMap.keySet().size() == 0) {
            return;
        }

        BaseWebSocketByteEventBO<BaseLiveRoomDataAddDataDTO> baseWebSocketByteEventBO =
            new BaseWebSocketByteEventBO<>();

        baseWebSocketByteEventBO.setUserIdSet(socketMap.keySet());

        baseWebSocketByteEventBO.setBaseSocketRefUserIdSet(CollUtil.newHashSet(socketMap.values()));

        WebSocketMessageDTO<BaseLiveRoomDataAddDataDTO> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_LIVE_ROOM_NEW_DATA, dto);

        baseWebSocketByteEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送：webSocket事件
        TempKafkaUtil.sendBaseWebSocketByteEventTopic(baseWebSocketByteEventBO, byteArr);

    }

}
