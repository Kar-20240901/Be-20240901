package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyGroupExtraMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupExtraDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageSelfDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageSelfVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImApplyGroupService;
import com.kar20240901.be.base.web.service.im.BaseImGroupRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImApplyGroupServiceImpl extends ServiceImpl<BaseImApplyGroupMapper, BaseImApplyGroupDO>
    implements BaseImApplyGroupService {

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    @Resource
    BaseFileService baseFileService;

    @Resource
    BaseImBlockMapper baseImBlockMapper;

    @Resource
    BaseImGroupRefUserMapper baseImGroupRefUserMapper;

    @Resource
    BaseImApplyGroupExtraMapper baseImApplyGroupExtraMapper;

    @Resource
    BaseImSessionRefUserService baseImSessionRefUserService;

    @Resource
    BaseImGroupRefUserService baseImGroupRefUserService;

    /**
     * 搜索要添加的群组
     */
    @Override
    public Page<BaseImApplyFriendSearchApplyGroupVO> searchApplyGroup(BaseImApplyFriendSearchApplyGroupDTO dto) {

        String name = dto.getName();

        Long groupId = dto.getGroupId();

        Page<BaseImApplyFriendSearchApplyGroupVO> resPage = new Page<>();

        if (StrUtil.isBlank(name) && groupId == null) {
            return resPage;
        }

        Page<BaseImGroupDO> page =
            ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(groupId != null, BaseImGroupDO::getId, groupId)
                .like(StrUtil.isNotBlank(name), BaseImGroupDO::getName, name)
                .select(BaseImGroupDO::getId, BaseImGroupDO::getName, BaseImGroupDO::getAvatarFileId)
                .page(dto.createTimeDescDefaultOrderPage(true));

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImGroupDO::getAvatarFileId).filter(it -> it != TempConstant.NEGATIVE_ONE)
                .collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        List<BaseImApplyFriendSearchApplyGroupVO> list = new ArrayList<>();

        for (BaseImGroupDO item : page.getRecords()) {

            BaseImApplyFriendSearchApplyGroupVO baseImApplyFriendSearchApplyGroupVO =
                new BaseImApplyFriendSearchApplyGroupVO();

            baseImApplyFriendSearchApplyGroupVO.setGroupId(item.getId());

            baseImApplyFriendSearchApplyGroupVO.setName(item.getName());

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            baseImApplyFriendSearchApplyGroupVO.setAvatarUrl(avatarUrl);

            list.add(baseImApplyFriendSearchApplyGroupVO);

        }

        resPage.setTotal(page.getTotal());

        resPage.setRecords(list);

        return resPage;

    }

    /**
     * 发送入群申请
     */
    @Override
    @DSTransactional
    public String send(BaseImApplyGroupSendDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseImGroupDO baseImGroupDO =
            ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, dto.getId())
                .select(BaseImGroupDO::getSessionId).one();

        if (baseImGroupDO == null) {
            R.error("操作失败：该群组不存在", dto.getId());
        }

        boolean existsBlock =
            ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getUserId, currentUserId)
                .eq(BaseImBlockDO::getSourceId, dto.getId()).eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.GROUP)
                .exists();

        if (existsBlock) {
            R.error("操作失败：您已被群组拉黑，无法添加", dto.getId());
        }

        String lockKey = BaseRedisKeyEnum.PRE_IM_APPLY_GROUP + ":" + currentUserId + ":" + dto.getId();

        RedissonUtil.doLock(lockKey, () -> {

            BaseImApplyGroupDO baseImApplyGroupDO = lambdaQuery().eq(BaseImApplyGroupDO::getUserId, currentUserId)
                .eq(BaseImApplyGroupDO::getTargetGroupId, dto.getId()).one();

            if (baseImApplyGroupDO == null) {

                baseImApplyGroupDO = new BaseImApplyGroupDO();

                baseImApplyGroupDO.setUserId(currentUserId);
                baseImApplyGroupDO.setTargetGroupId(dto.getId());
                baseImApplyGroupDO.setSessionId(baseImGroupDO.getSessionId());

            } else {

                BaseImApplyStatusEnum baseImApplyStatusEnum = baseImApplyGroupDO.getStatus();

                if (BaseImApplyStatusEnum.PASSED.equals(baseImApplyStatusEnum)) {

                    // 查询：是否存在于群友列表里面
                    boolean existsGroup = ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper)
                        .eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                        .eq(BaseImGroupRefUserDO::getGroupId, dto.getId()).exists();

                    if (existsGroup) {

                        R.error("操作失败：您已经在群里了", dto.getId());

                    }

                }

                // 显示好友申请
                ChainWrappers.lambdaUpdateChain(baseImApplyGroupExtraMapper)
                    .eq(BaseImApplyGroupExtraDO::getApplyGroupId, baseImApplyGroupDO.getId())
                    .set(BaseImApplyGroupExtraDO::getHiddenFlag, false).update();

            }

            baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.APPLYING);
            baseImApplyGroupDO.setRejectReason("");
            baseImApplyGroupDO.setApplyTime(new Date());
            baseImApplyGroupDO.setApplyContent(dto.getApplyContent());

            saveOrUpdate(baseImApplyGroupDO);

        });

        // 通知：您有新的入群申请

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImApplyGroupPageSelfVO> myPageSelf(BaseImApplyGroupPageSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Page<BaseImApplyGroupPageSelfVO> page = baseMapper.myPageSelf(dto.pageOrder(), dto, currentUserId);

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImApplyGroupPageSelfVO::getAvatarFileId).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseImApplyGroupPageSelfVO item : page.getRecords()) {

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            item.setAvatarFileId(null);

            item.setAvatarUrl(avatarUrl);

        }

        return page;

    }

    /**
     * 分页排序查询-群组的入群申请
     */
    @Override
    public Page<BaseImApplyGroupPageGroupVO> myPageGroup(BaseImApplyGroupPageGroupDTO dto) {

        // 检查：是否有权限
        BaseImGroupUtil.checkGroupAuth(dto.getGroupId());

        Page<BaseImApplyGroupPageGroupVO> page = baseMapper.myPageGroup(dto.pageOrder(), dto);

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImApplyGroupPageGroupVO::getAvatarFileId).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseImApplyGroupPageGroupVO item : page.getRecords()) {

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            item.setAvatarFileId(null);

            item.setAvatarUrl(avatarUrl);

        }

        return page;

    }

    /**
     * 同意
     */
    @Override
    @DSTransactional
    public String agree(NotNullId dto) {

        String lockKey = BaseRedisKeyEnum.PRE_IM_APPLY_GROUP_ID + ":" + dto.getId();

        RedissonUtil.doLock(lockKey, () -> {

            BaseImApplyGroupDO baseImApplyGroupDO = lambdaQuery().eq(BaseImApplyGroupDO::getId, dto.getId()).one();

            if (baseImApplyGroupDO == null) {
                R.error("操作失败：入群申请不存在", dto.getId());
            }

            // 检查：是否有权限
            BaseImGroupUtil.checkGroupAuth(baseImApplyGroupDO.getTargetGroupId());

            if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyGroupDO.getStatus())) {
                R.error("操作失败：该入群申请状态已发生改变，请刷新再试", dto.getId());
            }

            baseImApplyGroupDO.setRejectReason("");

            baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.PASSED);

            // 更新数据
            updateById(baseImApplyGroupDO);

            // 显示入群申请
            ChainWrappers.lambdaUpdateChain(baseImApplyGroupExtraMapper)
                .eq(BaseImApplyGroupExtraDO::getApplyGroupId, baseImApplyGroupDO.getId())
                .set(BaseImApplyGroupExtraDO::getHiddenFlag, false).update();

            // 创建会话关联用户
            baseImSessionRefUserService.addOrUpdateSessionRefUserForGroup(baseImApplyGroupDO.getSessionId(),
                baseImApplyGroupDO.getTargetGroupId(), baseImApplyGroupDO.getUserId());

            // 添加群员
            baseImGroupRefUserService.addUser(baseImApplyGroupDO.getSessionId(), baseImApplyGroupDO.getTargetGroupId(),
                baseImApplyGroupDO.getUserId());

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 拒绝
     */
    @Override
    @DSTransactional
    public String reject(BaseImApplyGroupRejectDTO dto) {

        String lockKey = BaseRedisKeyEnum.PRE_IM_APPLY_GROUP_ID + ":" + dto.getId();

        RedissonUtil.doLock(lockKey, () -> {

            BaseImApplyGroupDO baseImApplyGroupDO = lambdaQuery().eq(BaseImApplyGroupDO::getId, dto.getId()).one();

            if (baseImApplyGroupDO == null) {
                R.error("操作失败：入群申请不存在", dto.getId());
            }

            // 检查：是否有权限
            BaseImGroupUtil.checkGroupAuth(baseImApplyGroupDO.getTargetGroupId());

            if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyGroupDO.getStatus())) {
                R.error("操作失败：该入群申请状态已发生改变，请刷新再试", dto.getId());
            }

            baseImApplyGroupDO.setRejectReason(MyEntityUtil.getNotNullStr(dto.getRejectReason()));

            baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.REJECTED);

            // 更新数据
            updateById(baseImApplyGroupDO);

            // 显示入群申请
            ChainWrappers.lambdaUpdateChain(baseImApplyGroupExtraMapper)
                .eq(BaseImApplyGroupExtraDO::getApplyGroupId, baseImApplyGroupDO.getId())
                .set(BaseImApplyGroupExtraDO::getHiddenFlag, false).update();

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏
     */
    @Override
    public String hidden(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists =
            lambdaQuery().eq(BaseImApplyGroupDO::getUserId, currentUserId).eq(BaseImApplyGroupDO::getId, dto.getId())
                .exists();

        if (!exists) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        baseImApplyGroupExtraMapper.insertOrUpdateHiddenFlag(dto.getId(), currentUserId, true);

        return TempBizCodeEnum.OK;

    }

}
