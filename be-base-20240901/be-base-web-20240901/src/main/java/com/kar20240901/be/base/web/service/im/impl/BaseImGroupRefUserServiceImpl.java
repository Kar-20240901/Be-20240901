package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserAddMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserDeleteMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserMutePageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserPageDTO;
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

        Page<BaseImGroupRefUserPageVO> page = baseMapper.myPage(dto.createTimeDescDefaultOrderPage(), dto);

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
     * 群组分页排序查询禁言用户
     */
    @Override
    public Page<BaseImGroupRefUserPageVO> pageMute(BaseImGroupRefUserMutePageDTO dto) {

        // 检测权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId(), false);

        Page<BaseImGroupRefUserPageVO> page = baseMapper.pageMute(dto.updateTimeDescDefaultOrderPage(), dto);

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

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getUserIdSet().contains(currentUserId)) {
            R.errorMsg("操作失败：不能禁言自己");
        }

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

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getUserIdSet().contains(currentUserId)) {
            R.errorMsg("操作失败：不能解除自己的禁言");
        }

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
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId(), true);

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
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId(), true);

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getUserIdSet().contains(currentUserId)) {
            R.errorMsg("操作失败：不能解除自己的管理员");
        }

        lambdaUpdate().eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .in(BaseImGroupRefUserDO::getUserId, dto.getUserIdSet()).set(BaseImGroupRefUserDO::getManageFlag, false)
            .update();

        return TempBizCodeEnum.OK;

    }

}
