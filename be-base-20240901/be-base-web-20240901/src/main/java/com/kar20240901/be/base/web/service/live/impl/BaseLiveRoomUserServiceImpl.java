package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketStrEventBO;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserAddUserDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomUserServiceImpl extends ServiceImpl<BaseLiveRoomUserMapper, BaseLiveRoomUserDO>
    implements BaseLiveRoomUserService {

    @Resource
    BaseLiveRoomMapper baseLiveRoomMapper;

    /**
     * 定时任务，检查房间内的用户
     */
    @PreDestroy
    @Scheduled(fixedDelay = 3000)
    public void scheduledCheckRoomUser() {

        List<Long> roomUserIdList = baseMapper.checkRoomUser();

        if (CollUtil.isEmpty(roomUserIdList)) {
            return;
        }

        removeBatchByIds(roomUserIdList);

        List<BaseLiveRoomUserDO> baseLiveRoomUserDoList = lambdaQuery().in(BaseLiveRoomUserDO::getId, roomUserIdList)
            .select(BaseLiveRoomUserDO::getUserId, BaseLiveRoomUserDO::getSocketRefUserId).list();

        if (CollUtil.isEmpty(baseLiveRoomUserDoList)) {
            return;
        }

        Set<Long> userIdSet = new HashSet<>(baseLiveRoomUserDoList.size());

        Set<Long> baseSocketRefUserIdSet = new HashSet<>(baseLiveRoomUserDoList.size());

        for (BaseLiveRoomUserDO item : baseLiveRoomUserDoList) {

            userIdSet.add(item.getUserId());

            baseSocketRefUserIdSet.add(item.getSocketRefUserId());

        }

        BaseWebSocketStrEventBO<Long> baseWebSocketStrEventBO = new BaseWebSocketStrEventBO<>();

        baseWebSocketStrEventBO.setUserIdSet(userIdSet);

        baseWebSocketStrEventBO.setBaseSocketRefUserIdSet(baseSocketRefUserIdSet);

        WebSocketMessageDTO<Long> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_LIVE_ROOM_REMOVE_USER, null);

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送消息，您已经在其他设备上加入此房间
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

    }

    /**
     * 新增用户，备注：不用加事务
     */
    @Override
    public String addUser(BaseLiveRoomUserAddUserDTO dto, ChannelDataBO channelDataBO) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseLiveRoomDO baseLiveRoomDO =
            ChainWrappers.lambdaQueryChain(baseLiveRoomMapper).eq(BaseLiveRoomDO::getId, dto.getId())
                .select(BaseLiveRoomDO::getCode, BaseLiveRoomDO::getBelongId).one();

        if (baseLiveRoomDO == null) {
            R.error("操作失败：该房间不存在", dto.getId());
        }

        if (!currentUserId.equals(baseLiveRoomDO.getBelongId())) {

            if (StrUtil.isNotBlank(baseLiveRoomDO.getCode())) {

                // 如果验证码不匹配
                if (baseLiveRoomDO.getCode().equalsIgnoreCase(dto.getCode()) == false) {
                    R.error("操作失败：房间验证码错误", dto.getCode());
                }

            }

        }

        // 处理：您已经在其他设备上加入此房间
        handleJoinOnOtherDevice(currentUserId, dto.getId());

        // 处理：有新用户加入
        handleNewUserJoin(dto, currentUserId);

        BaseLiveRoomUserDO baseLiveRoomUserDO = new BaseLiveRoomUserDO();

        baseLiveRoomUserDO.setRoomId(dto.getId());
        baseLiveRoomUserDO.setUserId(channelDataBO.getUserId());
        baseLiveRoomUserDO.setSocketRefUserId(channelDataBO.getSocketRefUserId());

        save(baseLiveRoomUserDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 处理：有新用户加入
     */
    private void handleNewUserJoin(BaseLiveRoomUserAddUserDTO dto, Long currentUserId) {

        List<BaseLiveRoomUserDO> baseLiveRoomUserDoList = lambdaQuery().eq(BaseLiveRoomUserDO::getRoomId, dto.getId())
            .select(BaseLiveRoomUserDO::getUserId, BaseLiveRoomUserDO::getSocketRefUserId)
            .ne(BaseLiveRoomUserDO::getUserId, currentUserId).list();

        if (CollUtil.isEmpty(baseLiveRoomUserDoList)) {
            return;
        }

        Set<Long> userIdSet = new HashSet<>(baseLiveRoomUserDoList.size());

        Set<Long> baseSocketRefUserIdSet = new HashSet<>(baseLiveRoomUserDoList.size());

        for (BaseLiveRoomUserDO item : baseLiveRoomUserDoList) {

            userIdSet.add(item.getUserId());

            baseSocketRefUserIdSet.add(item.getSocketRefUserId());

        }

        BaseWebSocketStrEventBO<Long> baseWebSocketStrEventBO = new BaseWebSocketStrEventBO<>();

        baseWebSocketStrEventBO.setUserIdSet(userIdSet);

        baseWebSocketStrEventBO.setBaseSocketRefUserIdSet(baseSocketRefUserIdSet);

        WebSocketMessageDTO<Long> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_LIVE_ROOM_NEW_USER, null);

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送消息，有新用户加入
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

    }

    /**
     * 处理：您已经在其他设备上加入此房间
     */
    private void handleJoinOnOtherDevice(Long currentUserId, Long roomId) {

        List<BaseLiveRoomUserDO> baseLiveRoomUserDoList =
            lambdaQuery().eq(BaseLiveRoomUserDO::getUserId, currentUserId).eq(BaseLiveRoomUserDO::getRoomId, roomId)
                .select(BaseLiveRoomUserDO::getId, BaseLiveRoomUserDO::getSocketRefUserId,
                    BaseLiveRoomUserDO::getUserId).list();

        if (CollUtil.isEmpty(baseLiveRoomUserDoList)) {
            return;
        }

        Set<Long> userIdSet = new HashSet<>();

        Set<Long> baseSocketRefUserIdSet = new HashSet<>(baseLiveRoomUserDoList.size());

        List<Long> baseLiveRoomUserIdList = new ArrayList<>(baseLiveRoomUserDoList.size());

        for (BaseLiveRoomUserDO item : baseLiveRoomUserDoList) {

            userIdSet.add(item.getUserId());

            baseSocketRefUserIdSet.add(item.getSocketRefUserId());

            baseLiveRoomUserIdList.add(item.getId());

        }

        removeBatchByIds(baseLiveRoomUserIdList);

        BaseWebSocketStrEventBO<Long> baseWebSocketStrEventBO = new BaseWebSocketStrEventBO<>();

        baseWebSocketStrEventBO.setUserIdSet(userIdSet);

        baseWebSocketStrEventBO.setBaseSocketRefUserIdSet(baseSocketRefUserIdSet);

        WebSocketMessageDTO<Long> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_LIVE_ROOM_JOIN_ON_OTHER_DEVICE, null);

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送消息，您已经在其他设备上加入此房间
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

    }

}
