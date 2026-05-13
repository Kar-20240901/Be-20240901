package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.exception.im.BaseImBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketStrEventBO;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
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
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
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
    BaseImSessionContentRefUserService baseImSessionContentRefUserService;

    @Resource
    BaseImSessionMapper baseImSessionMapper;

    /**
     * 新增文字消息
     */
    @Override
    @DSTransactional
    public BaseImSessionContentRefUserPageVO insertTxt(BaseImSessionContentInsertTxtDTO dto) {

        IBaseImSessionContentType iBaseImSessionContentType = BaseImSessionContentTypeEnum.MAP.get(dto.getType());

        if (iBaseImSessionContentType == null) {
            R.error(BaseImBizCodeEnum.MSG_TYPE_NOT_EXIST, dto.getType());
        }

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long sessionId = dto.getSessionId();

        BaseImSessionRefUserDO baseImSessionRefUserDO = ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId).eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .select(BaseImSessionRefUserDO::getTargetType, BaseImSessionRefUserDO::getTargetId).one();

        if (baseImSessionRefUserDO == null) {
            R.error(BaseImBizCodeEnum.SESSION_INFO_NOT_EXIST, sessionId);
        }

        Integer targetType = baseImSessionRefUserDO.getTargetType();

        IBaseImType iBaseImType = BaseImTypeEnum.MAP.get(targetType);

        if (iBaseImType == null) {
            R.error(BaseImBizCodeEnum.SESSION_TYPE_NOT_EXIST, targetType);
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
        return doInsertTxt(dto, sessionId, iBaseImSessionContentType, true, currentUserId);

    }

    /**
     * 执行：新增文字消息
     */
    public BaseImSessionContentRefUserPageVO doInsertTxt(BaseImSessionContentInsertTxtDTO dto, Long sessionId,
        IBaseImSessionContentType iBaseImSessionContentType, boolean updateLastOpenTsFlag, Long createId) {

        Date date = new Date();

        BaseImSessionContentDO baseImSessionContentDO = new BaseImSessionContentDO();

        baseImSessionContentDO.setEnableFlag(true);
        baseImSessionContentDO.setSessionId(sessionId);
        baseImSessionContentDO.setContent(dto.getTxt());
        baseImSessionContentDO.setType(iBaseImSessionContentType.getCode());
        baseImSessionContentDO.setCreateTs(dto.getCreateTs());
        baseImSessionContentDO.setRefId(MyEntityUtil.getNotNullLong(dto.getRefId()));
        baseImSessionContentDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));
        baseImSessionContentDO.setCreateId(createId);
        baseImSessionContentDO.setCreateTime(date);

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

        if (updateLastOpenTsFlag) {

            // 更新最后一次打开会话的时间
            BaseImSessionContentRefUserServiceImpl.updateLastOpenTs(createId, CollUtil.newHashSet(sessionId));

        }

        // 显示会话
        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper).eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .set(BaseImSessionRefUserDO::getShowFlag, true).update();

        dto.setNotDisturbFlagUserIdSet(notDisturbFlagUserIdSet);

        dto.setContentId(baseImSessionContentDO.getId());

        BaseWebSocketStrEventBO<BaseImSessionContentInsertTxtDTO> baseWebSocketStrEventBO =
            new BaseWebSocketStrEventBO<>();

        baseWebSocketStrEventBO.setUserIdSet(sessionRefUserMap.keySet());

        baseWebSocketStrEventBO.setBaseSocketRefUserIdSet(null);

        WebSocketMessageDTO<BaseImSessionContentInsertTxtDTO> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_IM_SESSION_CONTENT_SEND, dto);

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 通知用户：有新消息
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

        BaseImSessionContentRefUserPageVO baseImSessionContentRefUserPageVO = new BaseImSessionContentRefUserPageVO();

        baseImSessionContentRefUserPageVO.setContentId(contentId);
        baseImSessionContentRefUserPageVO.setCreateTs(baseImSessionContentDO.getCreateTs());
        baseImSessionContentRefUserPageVO.setSessionId(baseImSessionContentDO.getSessionId());
        baseImSessionContentRefUserPageVO.setOrderNo(baseImSessionContentDO.getOrderNo());

        return baseImSessionContentRefUserPageVO;

    }

    /**
     * 发送文字：群组
     */
    private void insertTxtForGroupCheck(Long currentUserId, Long targetId, IBaseImType baseImTypeEnum) {

        BaseImGroupUtil.checkMuteFlag(targetId);

        boolean exists = ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, targetId)
            .eq(BaseImBlockDO::getUserId, currentUserId).eq(BaseImBlockDO::getSourceType, baseImTypeEnum.getCode())
            .exists();

        if (exists) {
            R.error(BaseImBizCodeEnum.TARGET_REFUSE_RECEIVE_MSG, targetId);
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
            R.error(BaseImBizCodeEnum.TARGET_NOT_YOUR_FRIEND, targetId);
        }

        exists = ChainWrappers.lambdaQueryChain(baseImFriendMapper).eq(BaseImFriendDO::getBelongId, targetId)
            .eq(BaseImFriendDO::getFriendId, currentUserId).exists();

        if (!exists) {
            R.error(BaseImBizCodeEnum.YOU_NOT_TARGET_FRIEND, targetId);
        }

        exists = ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, targetId)
            .eq(BaseImBlockDO::getUserId, currentUserId).eq(BaseImBlockDO::getSourceType, baseImTypeEnum.getCode())
            .exists();

        if (exists) {
            R.error(BaseImBizCodeEnum.TARGET_REFUSE_RECEIVE_MSG, targetId);
        }

    }

    /**
     * 增加添加成功的消息内容
     */
    @Override
    @DSTransactional
    public void addApplyFriendFinishContent(Long sessionId, Long sourceUserId, Date date) {

        BaseImSessionContentInsertTxtDTO baseImSessionContentInsertTxtDTO = new BaseImSessionContentInsertTxtDTO();
        baseImSessionContentInsertTxtDTO.setSessionId(sessionId);
        baseImSessionContentInsertTxtDTO.setTxt("");
        baseImSessionContentInsertTxtDTO.setCreateTs(date.getTime());
        baseImSessionContentInsertTxtDTO.setRefId(TempConstant.NEGATIVE_ONE);
        baseImSessionContentInsertTxtDTO.setOrderNo(MyEntityUtil.getNotNullOrderNo(null));

        // 添加消息
        doInsertTxt(baseImSessionContentInsertTxtDTO, sessionId, BaseImSessionContentTypeEnum.TEXT_FRIEND_APPLY_FINISH,
            false, sourceUserId);

    }

    /**
     * 增加创建成功的消息内容
     */
    @Override
    @DSTransactional
    public void addInsertGroupFinishContent(Long sessionId, Long createUserId, Date date) {

        BaseImSessionContentInsertTxtDTO baseImSessionContentInsertTxtDTO = new BaseImSessionContentInsertTxtDTO();
        baseImSessionContentInsertTxtDTO.setSessionId(sessionId);
        baseImSessionContentInsertTxtDTO.setTxt("");
        baseImSessionContentInsertTxtDTO.setCreateTs(date.getTime());
        baseImSessionContentInsertTxtDTO.setRefId(TempConstant.NEGATIVE_ONE);
        baseImSessionContentInsertTxtDTO.setOrderNo(MyEntityUtil.getNotNullOrderNo(null));

        // 添加消息
        doInsertTxt(baseImSessionContentInsertTxtDTO, sessionId, BaseImSessionContentTypeEnum.TEXT_GROUP_CREATE_FINISH,
            false, createUserId);

    }

    /**
     * 增加入群成功的消息内容
     */
    @Override
    @DSTransactional
    public void addApplyGroupFinishContent(Long sessionId, Long sourceUserId, Long targetUserId, Date date,
        String txt) {

        BaseImSessionContentInsertTxtDTO baseImSessionContentInsertTxtDTO = new BaseImSessionContentInsertTxtDTO();
        baseImSessionContentInsertTxtDTO.setSessionId(sessionId);
        baseImSessionContentInsertTxtDTO.setTxt(MyEntityUtil.getNotNullStr(txt));
        baseImSessionContentInsertTxtDTO.setCreateTs(date.getTime());
        baseImSessionContentInsertTxtDTO.setRefId(sourceUserId);
        baseImSessionContentInsertTxtDTO.setOrderNo(MyEntityUtil.getNotNullOrderNo(null));

        // 添加消息
        doInsertTxt(baseImSessionContentInsertTxtDTO, sessionId, BaseImSessionContentTypeEnum.TEXT_GROUP_APPLY_FINISH,
            false, targetUserId);

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
