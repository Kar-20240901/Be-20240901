package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupAddUserDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImBlockGroupPageVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImBlockService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImBlockServiceImpl extends ServiceImpl<BaseImBlockMapper, BaseImBlockDO>
    implements BaseImBlockService {

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    @Resource
    BaseImApplyFriendMapper baseImApplyFriendMapper;

    @Resource
    BaseFileService baseFileService;

    /**
     * 拉黑好友
     */
    @Override
    public String addFriend(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getId().equals(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        BaseImApplyFriendDO baseImApplyFriendDO = ChainWrappers.lambdaQueryChain(baseImApplyFriendMapper).and(
                i -> i.eq(BaseImApplyFriendDO::getUserId, currentUserId)
                    .or(o -> o.eq(BaseImApplyFriendDO::getTargetUserId, currentUserId)))
            .select(BaseImApplyFriendDO::getSessionId).one();

        Long sessionId = TempConstant.NEGATIVE_ONE;

        if (baseImApplyFriendDO != null) {

            sessionId = baseImApplyFriendDO.getSessionId();

        }

        BaseImBlockDO baseImBlockDO = new BaseImBlockDO();

        baseImBlockDO.setUserId(dto.getId());
        baseImBlockDO.setSessionId(sessionId);
        baseImBlockDO.setSourceId(currentUserId);
        baseImBlockDO.setSourceType(BaseImTypeEnum.FRIEND.getCode());

        save(baseImBlockDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 群组拉黑用户
     */
    @Override
    public String groupAddUser(BaseImBlockGroupAddUserDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long groupId = dto.getGroupId();

        Long userId = dto.getUserId();

        if (userId.equals(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        BaseImGroupDO baseImGroupDO =
            ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, groupId)
                .eq(BaseImGroupDO::getBelongId, currentUserId).select(BaseImGroupDO::getSessionId).one();

        if (baseImGroupDO == null) {
            R.error(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);
        }

        BaseImBlockDO baseImBlockDO = new BaseImBlockDO();

        baseImBlockDO.setCreateId(currentUserId);
        baseImBlockDO.setUserId(userId);
        baseImBlockDO.setSessionId(baseImGroupDO.getSessionId());
        baseImBlockDO.setSourceId(groupId);
        baseImBlockDO.setSourceType(BaseImTypeEnum.GROUP.getCode());

        save(baseImBlockDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消拉黑好友
     */
    @Override
    public String cancelFriend(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getId().equals(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        lambdaUpdate().eq(BaseImBlockDO::getUserId, dto.getId()).eq(BaseImBlockDO::getSourceId, currentUserId)
            .eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.FRIEND).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消群组拉黑用户
     */
    @Override
    public String groupCancelUser(BaseImBlockGroupAddUserDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long groupId = dto.getGroupId();

        Long userId = dto.getUserId();

        if (userId.equals(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        boolean exists = ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, groupId)
            .eq(BaseImGroupDO::getBelongId, currentUserId).exists();

        if (!exists) {
            R.error(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);
        }

        lambdaUpdate().eq(BaseImBlockDO::getUserId, userId).eq(BaseImBlockDO::getSourceId, groupId)
            .eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.GROUP).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 群组分页排序查询拉黑用户
     */
    @Override
    public Page<BaseImBlockGroupPageVO> groupPage(BaseImBlockGroupPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long groupId = dto.getGroupId();

        boolean exists = ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, groupId)
            .eq(BaseImGroupDO::getBelongId, currentUserId).exists();

        if (!exists) {
            R.error(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);
        }

        dto.setSourceType(BaseImTypeEnum.GROUP.getCode());

        Page<BaseImBlockGroupPageVO> resPage = baseMapper.groupPage(dto.createTimeDescDefaultOrderPage(), dto);

        Set<Long> avatarIdSet =
            resPage.getRecords().stream().map(BaseImBlockGroupPageVO::getAvatarFileId).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarIdSet)).getMap();

        for (BaseImBlockGroupPageVO item : resPage.getRecords()) {

            Long avatarFileId = item.getAvatarFileId();

            String avatarUrl = publicUrlMap.get(avatarFileId);

            item.setAvatarFileId(null);

            item.setAvatarUrl(avatarUrl);

        }

        return resPage;

    }

}
