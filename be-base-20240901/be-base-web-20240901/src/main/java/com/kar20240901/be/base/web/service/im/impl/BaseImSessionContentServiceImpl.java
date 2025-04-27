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
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentInsertTxtDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
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

    /**
     * 新增文字消息
     */
    @Override
    public String insertTxt(BaseImSessionContentInsertTxtDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseImSessionRefUserDO baseImSessionRefUserDO = ChainWrappers.lambdaQueryChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .eq(BaseImSessionRefUserDO::getSessionId, dto.getSessionId())
            .select(BaseImSessionRefUserDO::getTargetType, BaseImSessionRefUserDO::getTargetId).one();

        if (baseImSessionRefUserDO == null) {
            R.error("操作失败：会话信息不存在", dto.getSessionId());
        }

        Integer targetType = baseImSessionRefUserDO.getTargetType();

        BaseImTypeEnum baseImTypeEnum = BaseImTypeEnum.MAP.get(targetType);

        if (baseImTypeEnum == null) {
            R.error("操作失败：会话类型不存在", targetType);
        }

        Long targetId = baseImSessionRefUserDO.getTargetId();

        if (BaseImTypeEnum.FRIEND.equals(baseImTypeEnum)) {

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

        } else if (BaseImTypeEnum.GROUP.equals(baseImTypeEnum)) {

            BaseImGroupRefUserDO baseImGroupRefUserDO = ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper)
                .eq(BaseImGroupRefUserDO::getUserId, currentUserId).eq(BaseImGroupRefUserDO::getGroupId, targetId)
                .select(BaseImGroupRefUserDO::getMuteFlag).one();

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

        return TempBizCodeEnum.OK;

    }

}
