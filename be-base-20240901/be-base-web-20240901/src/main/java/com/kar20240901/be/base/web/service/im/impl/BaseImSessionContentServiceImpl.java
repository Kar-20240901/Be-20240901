package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketStrEventBO;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentUpdateTargetInputFlagDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImSessionContentTypeEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImSessionContentType;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImType;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionContentServiceImpl extends ServiceImpl<BaseImSessionContentMapper, BaseImSessionContentDO>
    implements BaseImSessionContentService {

    @Resource
    BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    @Resource
    BaseImFriendMapper baseImFriendMapper;

    @Resource
    BaseImBlockMapper baseImBlockMapper;

    @Resource
    BaseImGroupRefUserMapper baseImGroupRefUserMapper;

    @Resource
    BaseImSessionContentRefUserService baseImSessionContentRefUserService;

    /**
     * 新增文字消息
     */
    @Override
    @DSTransactional
    public String insertTxt(BaseImSessionContentInsertTxtDTO dto) {

        IBaseImSessionContentType iBaseImSessionContentType = BaseImSessionContentTypeEnum.MAP.get(dto.getType());

        if (iBaseImSessionContentType == null) {
            R.error("操作失败：消息类型不存在", dto.getType());
        }

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long sessionId = dto.getSessionId();

        BaseImSessionRefUserDO baseImSessionRefUserDO = ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId).eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .select(BaseImSessionRefUserDO::getTargetType, BaseImSessionRefUserDO::getTargetId).one();

        if (baseImSessionRefUserDO == null) {
            R.error("操作失败：会话信息不存在", sessionId);
        }

        Integer targetType = baseImSessionRefUserDO.getTargetType();

        IBaseImType iBaseImType = BaseImTypeEnum.MAP.get(targetType);

        if (iBaseImType == null) {
            R.error("操作失败：会话类型不存在", targetType);
        }

        Long targetId = baseImSessionRefUserDO.getTargetId();

        if (BaseImTypeEnum.FRIEND.getCode() == iBaseImType.getCode()) {

            // 发送文字：好友检查
            insertTxtForFriendCheck(currentUserId, targetId, iBaseImType);

        } else if (BaseImTypeEnum.GROUP.getCode() == iBaseImType.getCode()) {

            // 发送文字：群组检查
            insertTxtForGroupCheck(currentUserId, targetId, iBaseImType);

        }

        // 执行：发送消息
        doInsertTxt(dto, sessionId, iBaseImSessionContentType);

        return TempBizCodeEnum.OK;

    }

    /**
     * 执行：发送消息
     */
    public void doInsertTxt(BaseImSessionContentInsertTxtDTO dto, Long sessionId,
        IBaseImSessionContentType iBaseImSessionContentType) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        dto.setCreateId(currentUserId);

        BaseImSessionContentDO baseImSessionContentDO = new BaseImSessionContentDO();

        baseImSessionContentDO.setEnableFlag(true);
        baseImSessionContentDO.setSessionId(sessionId);
        baseImSessionContentDO.setContent(dto.getTxt());
        baseImSessionContentDO.setType(iBaseImSessionContentType.getCode());
        baseImSessionContentDO.setCreateTs(dto.getCreateTs());
        baseImSessionContentDO.setRefId(MyEntityUtil.getNotNullLong(dto.getRefId()));
        baseImSessionContentDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));
        baseImSessionContentDO.setCreateId(currentUserId);
        baseImSessionContentDO.setCreateTime(new Date());

        save(baseImSessionContentDO);

        Long contentId = baseImSessionContentDO.getId();

        List<BaseImSessionRefUserDO> baseImSessionRefUserDoList =
            ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
                .eq(BaseImSessionRefUserDO::getSessionId, sessionId)
                .select(BaseImSessionRefUserDO::getUserId, BaseImSessionRefUserDO::getNotDisturbFlag).list();

        Map<Long, BaseImSessionRefUserDO> sessionRefUserMap =
            baseImSessionRefUserDoList.stream().collect(Collectors.toMap(BaseImSessionRefUserDO::getUserId, it -> it));

        List<BaseImSessionContentRefUserDO> list = new ArrayList<>();

        Set<Long> notDisturbFlagUserIdSet = new HashSet<>();

        for (Entry<Long, BaseImSessionRefUserDO> item : sessionRefUserMap.entrySet()) {

            Long userId = item.getKey();

            BaseImSessionContentRefUserDO baseImSessionContentRefUserDO = new BaseImSessionContentRefUserDO();

            baseImSessionContentRefUserDO.setSessionId(sessionId);
            baseImSessionContentRefUserDO.setContentId(contentId);
            baseImSessionContentRefUserDO.setUserId(userId);
            baseImSessionContentRefUserDO.setShowFlag(true);

            list.add(baseImSessionContentRefUserDO);

            if (item.getValue().getNotDisturbFlag()) {
                notDisturbFlagUserIdSet.add(userId);
            }

        }

        baseImSessionContentRefUserService.saveBatch(list);

        dto.setNotDisturbFlagUserIdSet(notDisturbFlagUserIdSet);

        BaseWebSocketStrEventBO<BaseImSessionContentInsertTxtDTO> baseWebSocketStrEventBO =
            new BaseWebSocketStrEventBO<>();

        Set<Long> userIdSet = CollUtil.newHashSet(sessionRefUserMap.keySet());

        userIdSet.remove(currentUserId);

        baseWebSocketStrEventBO.setUserIdSet(userIdSet);

        baseWebSocketStrEventBO.setBaseSocketRefUserIdSet(null);

        WebSocketMessageDTO<BaseImSessionContentInsertTxtDTO> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_IM_SESSION_CONTENT_SEND, dto);

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 通知用户：有新消息
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

    }

    /**
     * 发送文字：群组
     */
    private void insertTxtForGroupCheck(Long currentUserId, Long targetId, IBaseImType baseImTypeEnum) {

        BaseImGroupRefUserDO baseImGroupRefUserDO =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                .eq(BaseImGroupRefUserDO::getGroupId, targetId).select(BaseImGroupRefUserDO::getMuteFlag).one();

        if (baseImGroupRefUserDO == null) {
            R.error("操作失败：您不是该群成员，无法发送消息", targetId);
        }

        if (BooleanUtil.isTrue(baseImGroupRefUserDO.getMuteFlag())) {
            R.error("操作失败：您已被禁言，无法发送消息", targetId);
        }

        boolean exists = ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, targetId)
            .eq(BaseImBlockDO::getUserId, currentUserId).eq(BaseImBlockDO::getSourceType, baseImTypeEnum.getCode())
            .exists();

        if (exists) {
            R.error("操作失败：对方拒绝接收您的消息，无法发送消息", targetId);
        }

    }

    /**
     * 发送文字：好友
     */
    private void insertTxtForFriendCheck(Long currentUserId, Long targetId, IBaseImType baseImTypeEnum) {

        boolean exists =
            ChainWrappers.lambdaQueryChain(baseImFriendMapper).eq(BaseImFriendDO::getBelongId, currentUserId)
                .eq(BaseImFriendDO::getFriendId, targetId).exists();

        if (!exists) {
            R.error("操作失败：对方不是您的好友，无法发送消息", targetId);
        }

        exists = ChainWrappers.lambdaQueryChain(baseImFriendMapper).eq(BaseImFriendDO::getBelongId, targetId)
            .eq(BaseImFriendDO::getFriendId, currentUserId).exists();

        if (!exists) {
            R.error("操作失败：您不是对方的好友，无法发送消息", targetId);
        }

        exists = ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, targetId)
            .eq(BaseImBlockDO::getUserId, currentUserId).eq(BaseImBlockDO::getSourceType, baseImTypeEnum.getCode())
            .exists();

        if (exists) {
            R.error("操作失败：对方拒绝接收您的消息，无法发送消息", targetId);
        }

    }

    /**
     * 修改为输入中
     */
    @Override
    public String updateTargetInputFlag(BaseImSessionContentUpdateTargetInputFlagDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long sessionId = dto.getSessionId();

        BaseImSessionRefUserDO baseImSessionRefUserDO = ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId).eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .select(BaseImSessionRefUserDO::getTargetType, BaseImSessionRefUserDO::getTargetId).one();

        if (baseImSessionRefUserDO == null) {
            R.error("操作失败：会话信息不存在", sessionId);
        }

        Integer targetType = baseImSessionRefUserDO.getTargetType();

        IBaseImType iBaseImType = BaseImTypeEnum.MAP.get(targetType);

        if (iBaseImType == null) {
            R.error("操作失败：会话类型不存在", targetType);
        }

        Long targetId = baseImSessionRefUserDO.getTargetId();

        if (BaseImTypeEnum.FRIEND.getCode() == iBaseImType.getCode()) {

            // 发送文字：好友检查
            insertTxtForFriendCheck(currentUserId, targetId, iBaseImType);

        } else if (BaseImTypeEnum.GROUP.getCode() == iBaseImType.getCode()) {

            return TempBizCodeEnum.OK;

        }

        BaseWebSocketStrEventBO<Long> baseWebSocketStrEventBO = new BaseWebSocketStrEventBO<>();

        baseWebSocketStrEventBO.setUserIdSet(CollUtil.newHashSet(targetId));

        baseWebSocketStrEventBO.setBaseSocketRefUserIdSet(null);

        WebSocketMessageDTO<Long> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_IM_SESSION_CONTENT_UPDATE_TARGET_INPUT_FLAG,
                sessionId);

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 通知用户：有新消息
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

        return TempBizCodeEnum.OK;

    }

}
