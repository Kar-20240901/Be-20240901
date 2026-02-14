package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserAddMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserDeleteMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupRefUserPageVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImGroupRefUserService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImGroupRefUserServiceImpl extends ServiceImpl<BaseImGroupRefUserMapper, BaseImGroupRefUserDO>
    implements BaseImGroupRefUserService {

    @Resource
    BaseFileService baseFileService;

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    @Resource
    BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    /**
     * 群组分页排序查询群员
     */
    @Override
    public Page<BaseImGroupRefUserPageVO> myPage(BaseImGroupRefUserPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists = lambdaQuery().eq(BaseImGroupRefUserDO::getUserId, currentUserId)
            .eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId()).exists();

        if (!exists) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, dto.getGroupId());
        }

        int sourceType = BaseImTypeEnum.GROUP.getCode();

        Page<BaseImGroupRefUserPageVO> page = baseMapper.myPage(dto.pageOrder(), dto, sourceType);

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImGroupRefUserPageVO::getAvatarFileId).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseImGroupRefUserPageVO item : page.getRecords()) {

            item.setAvatarUrl(publicUrlMap.get(item.getAvatarFileId()));

            item.setAvatarFileId(null);

        }

        return page;

    }

    /**
     * 新增禁言
     */
    @Override
    public String addMute(BaseImGroupRefUserAddMuteDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkForTargetUserId(dto.getGroupId(), dto.getUserIdSet());

        lambdaUpdate().eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .in(BaseImGroupRefUserDO::getUserId, dto.getUserIdSet()).set(BaseImGroupRefUserDO::getMuteFlag, true)
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 解除禁言
     */
    @Override
    public String deleteMute(BaseImGroupRefUserDeleteMuteDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkForTargetUserId(dto.getGroupId(), dto.getUserIdSet());

        lambdaUpdate().eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .in(BaseImGroupRefUserDO::getUserId, dto.getUserIdSet()).set(BaseImGroupRefUserDO::getMuteFlag, false)
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 添加群员
     */
    @Override
    public void addUser(Long sessionId, Long groupId, Long userId) {

        Assert.notNull(sessionId);

        Assert.notNull(groupId);

        Assert.notNull(userId);

        BaseImGroupRefUserDO baseImGroupRefUserDO = new BaseImGroupRefUserDO();

        baseImGroupRefUserDO.setGroupId(groupId);
        baseImGroupRefUserDO.setUserId(userId);
        baseImGroupRefUserDO.setMyNickname("");
        baseImGroupRefUserDO.setMuteFlag(false);
        baseImGroupRefUserDO.setManageFlag(false);

        save(baseImGroupRefUserDO);

    }

    /**
     * 新增管理员
     */
    @Override
    public String addManage(BaseImGroupRefUserAddMuteDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId(), true, true);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getUserIdSet().contains(currentUserId)) {
            R.errorMsg("操作失败：不能新增自己为管理员");
        }

        lambdaUpdate().eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .in(BaseImGroupRefUserDO::getUserId, dto.getUserIdSet()).set(BaseImGroupRefUserDO::getManageFlag, true)
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 解除管理员
     */
    @Override
    public String deleteManage(BaseImGroupRefUserDeleteMuteDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId(), true, true);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getUserIdSet().contains(currentUserId)) {
            R.errorMsg("操作失败：不能解除自己的管理员");
        }

        lambdaUpdate().eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .in(BaseImGroupRefUserDO::getUserId, dto.getUserIdSet()).set(BaseImGroupRefUserDO::getManageFlag, false)
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 群员退出-自我
     */
    @Override
    public String leaveSelf(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists = ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getBelongId, currentUserId)
            .in(BaseImGroupDO::getId, dto.getIdSet()).exists();

        if (exists) {
            R.error("操作失败：群主不能退出群聊", dto.getIdSet());
        }

        lambdaUpdate().in(BaseImGroupRefUserDO::getGroupId, dto.getIdSet())
            .eq(BaseImGroupRefUserDO::getUserId, currentUserId).remove();

        // 隐藏会话，注意：不能删除会话
        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper)
            .in(BaseImSessionRefUserDO::getTargetId, dto.getIdSet())
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .eq(BaseImSessionRefUserDO::getTargetType, BaseImTypeEnum.GROUP)
            .set(BaseImSessionRefUserDO::getShowFlag, false).update();

        return TempBizCodeEnum.OK;

    }

}
