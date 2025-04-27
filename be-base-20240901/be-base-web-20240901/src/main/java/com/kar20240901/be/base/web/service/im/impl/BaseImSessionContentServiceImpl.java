package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImSessionContentTypeEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    public String insertTxt(BaseImSessionContentInsertTxtDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long sessionId = dto.getSessionId();

        BaseImSessionRefUserDO baseImSessionRefUserDO = ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId).eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .select(BaseImSessionRefUserDO::getTargetType, BaseImSessionRefUserDO::getTargetId).one();

        if (baseImSessionRefUserDO == null) {
            R.error("操作失败：会话信息不存在", sessionId);
        }

        Integer targetType = baseImSessionRefUserDO.getTargetType();

        BaseImTypeEnum baseImTypeEnum = BaseImTypeEnum.MAP.get(targetType);

        if (baseImTypeEnum == null) {
            R.error("操作失败：会话类型不存在", targetType);
        }

        Long targetId = baseImSessionRefUserDO.getTargetId();

        if (BaseImTypeEnum.FRIEND.equals(baseImTypeEnum)) {

            // 发送文字：好友检查
            insertTxtForFriendCheck(currentUserId, targetId, baseImTypeEnum);

        } else if (BaseImTypeEnum.GROUP.equals(baseImTypeEnum)) {

            // 发送文字：群组检查
            insertTxtForGroupCheck(currentUserId, targetId, baseImTypeEnum);

        }

        // 执行：发送消息
        doInsertTxt(dto, sessionId);

        return TempBizCodeEnum.OK;

    }

    /**
     * 执行：发送消息
     */
    private void doInsertTxt(BaseImSessionContentInsertTxtDTO dto, Long sessionId) {

        BaseImSessionContentDO baseImSessionContentDO = new BaseImSessionContentDO();

        baseImSessionContentDO.setEnableFlag(true);
        baseImSessionContentDO.setSessionId(sessionId);
        baseImSessionContentDO.setContent(dto.getTxt());
        baseImSessionContentDO.setType(BaseImSessionContentTypeEnum.TEXT.getCode());
        baseImSessionContentDO.setCreateTs(dto.getCreateTs());
        baseImSessionContentDO.setRefId(MyEntityUtil.getNotNullLong(dto.getRefId()));
        baseImSessionContentDO.setOrderNo(MyEntityUtil.getNotNullOrderNo(dto.getOrderNo()));

        save(baseImSessionContentDO);

        Long contentId = baseImSessionContentDO.getId();

        List<BaseImSessionRefUserDO> baseImSessionRefUserDoList =
            ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
                .eq(BaseImSessionRefUserDO::getSessionId, sessionId)
                .select(BaseImSessionRefUserDO::getUserId, BaseImSessionRefUserDO::getNotDisturbFlag).list();

        Map<Long, BaseImSessionRefUserDO> sessionRefUserMap =
            baseImSessionRefUserDoList.stream().collect(Collectors.toMap(BaseImSessionRefUserDO::getUserId, it -> it));

        List<BaseImSessionContentRefUserDO> list = new ArrayList<>();

        for (Entry<Long, BaseImSessionRefUserDO> item : sessionRefUserMap.entrySet()) {

            Long userId = item.getKey();

            BaseImSessionContentRefUserDO baseImSessionContentRefUserDO = new BaseImSessionContentRefUserDO();

            baseImSessionContentRefUserDO.setSessionId(sessionId);
            baseImSessionContentRefUserDO.setContentId(contentId);
            baseImSessionContentRefUserDO.setUserId(userId);
            baseImSessionContentRefUserDO.setShowFlag(true);

            list.add(baseImSessionContentRefUserDO);

        }

        baseImSessionContentRefUserService.saveBatch(list);

        // 通知用户：有新消息

    }

    /**
     * 发送文字：群组
     */
    private void insertTxtForGroupCheck(Long currentUserId, Long targetId, BaseImTypeEnum baseImTypeEnum) {

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
            .eq(BaseImBlockDO::getUserId, currentUserId).eq(BaseImBlockDO::getSourceType, baseImTypeEnum).exists();

        if (exists) {
            R.error("操作失败：对方拒绝接收您的消息，无法发送消息", targetId);
        }

    }

    /**
     * 发送文字：好友
     */
    private void insertTxtForFriendCheck(Long currentUserId, Long targetId, BaseImTypeEnum baseImTypeEnum) {

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
            .eq(BaseImBlockDO::getUserId, currentUserId).eq(BaseImBlockDO::getSourceType, baseImTypeEnum).exists();

        if (exists) {
            R.error("操作失败：对方拒绝接收您的消息，无法发送消息", targetId);
        }

    }

}
