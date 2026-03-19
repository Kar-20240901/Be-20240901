package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
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
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupAgreeDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupHiddenGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageSelfDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSearchApplyGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageSelfVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupSearchApplyGroupVO;
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
import java.util.HashSet;
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
    public Page<BaseImApplyGroupSearchApplyGroupVO> searchApplyGroup(BaseImApplyGroupSearchApplyGroupDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        String searchKey = dto.getSearchKey();

        Page<BaseImApplyGroupSearchApplyGroupVO> resPage = new Page<>();

        List<BaseImGroupRefUserDO> baseImGroupRefUserDoList =
            ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                .select(BaseImGroupRefUserDO::getGroupId).list();

        Set<Long> notIdIdSet =
            baseImGroupRefUserDoList.stream().map(BaseImGroupRefUserDO::getGroupId).collect(Collectors.toSet());

        Page<BaseImGroupDO> page = ChainWrappers.lambdaQueryChain(baseImGroupMapper).or(StrUtil.isNotBlank(searchKey),
                i -> i.like(BaseImGroupDO::getName, searchKey).or().like(BaseImGroupDO::getUuid, searchKey)) //
            .notIn(CollUtil.isNotEmpty(notIdIdSet), BaseImGroupDO::getId, notIdIdSet) //
            .select(BaseImGroupDO::getId, BaseImGroupDO::getName, BaseImGroupDO::getAvatarFileId,
                BaseImGroupDO::getUuid, BaseImGroupDO::getBio).page(dto.idDescDefaultOrderPage(true));

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImGroupDO::getAvatarFileId).filter(it -> it != TempConstant.NEGATIVE_ONE)
                .collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        List<BaseImApplyGroupSearchApplyGroupVO> list = new ArrayList<>();

        for (BaseImGroupDO item : page.getRecords()) {

            BaseImApplyGroupSearchApplyGroupVO baseImApplyGroupSearchApplyGroupVO =
                new BaseImApplyGroupSearchApplyGroupVO();

            baseImApplyGroupSearchApplyGroupVO.setGroupId(item.getId());

            baseImApplyGroupSearchApplyGroupVO.setName(item.getName());

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            baseImApplyGroupSearchApplyGroupVO.setAvatarUrl(avatarUrl);

            baseImApplyGroupSearchApplyGroupVO.setUuid(item.getUuid());

            baseImApplyGroupSearchApplyGroupVO.setBio(item.getBio());

            list.add(baseImApplyGroupSearchApplyGroupVO);

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

        boolean singleFlag = dto.getIdSet().size() == 1;

        Long count = ChainWrappers.lambdaQueryChain(baseImGroupMapper).in(BaseImGroupDO::getId, dto.getIdSet())
            .select(BaseImGroupDO::getSessionId).count();

        if (count != dto.getIdSet().size()) {
            if (singleFlag) {
                R.error("操作失败：该群聊不存在", dto.getIdSet());
            } else {
                R.error("操作失败：群聊不存在，请刷新", dto.getIdSet());
            }
        }

        boolean existsBlock =
            ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getUserId, currentUserId)
                .in(BaseImBlockDO::getSourceId, dto.getIdSet()).eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.GROUP)
                .exists();

        if (existsBlock) {
            R.error("操作失败：您已被群聊拉黑，无法添加", dto.getIdSet());
        }

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_IM_APPLY_GROUP + ":" + currentUserId + ":", dto.getIdSet(),
            () -> {

                for (Long item : dto.getIdSet()) {

                    BaseImGroupDO baseImGroupDO =
                        ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, item)
                            .select(BaseImGroupDO::getSessionId).one();

                    if (baseImGroupDO == null) {
                        if (singleFlag) {
                            R.error("操作失败：该群聊不存在", item);
                        } else {
                            continue;
                        }
                    }

                    BaseImApplyGroupDO baseImApplyGroupDO =
                        lambdaQuery().eq(BaseImApplyGroupDO::getUserId, currentUserId)
                            .eq(BaseImApplyGroupDO::getTargetGroupId, item).one();

                    if (baseImApplyGroupDO == null) {

                        baseImApplyGroupDO = new BaseImApplyGroupDO();

                        baseImApplyGroupDO.setUserId(currentUserId);
                        baseImApplyGroupDO.setTargetGroupId(item);
                        baseImApplyGroupDO.setSessionId(baseImGroupDO.getSessionId());

                    } else {

                        BaseImApplyStatusEnum baseImApplyStatusEnum = baseImApplyGroupDO.getStatus();

                        if (BaseImApplyStatusEnum.PASSED.equals(baseImApplyStatusEnum)) {

                            // 查询：是否存在于群友列表里面
                            boolean existsGroup = ChainWrappers.lambdaQueryChain(baseImGroupRefUserMapper)
                                .eq(BaseImGroupRefUserDO::getUserId, currentUserId)
                                .eq(BaseImGroupRefUserDO::getGroupId, item).exists();

                            if (existsGroup) {

                                R.error("操作失败：您已经在群里了", item);

                            }

                        }

                        // 显示群组申请
                        ChainWrappers.lambdaUpdateChain(baseImApplyGroupExtraMapper)
                            .eq(BaseImApplyGroupExtraDO::getApplyGroupId, baseImApplyGroupDO.getId())
                            .set(BaseImApplyGroupExtraDO::getHiddenFlag, false).update();

                    }

                    Date date = new Date();

                    baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.APPLYING);
                    baseImApplyGroupDO.setRejectReason("");
                    baseImApplyGroupDO.setUpdateTime(date);
                    baseImApplyGroupDO.setApplyTime(date);
                    baseImApplyGroupDO.setApplyContent(MyEntityUtil.getNotNullStr(dto.getApplyContent()));

                    saveOrUpdate(baseImApplyGroupDO);

                    // 通知：您有新的入群申请

                }

            });

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImApplyGroupPageSelfVO> myPageSelf(BaseImApplyGroupPageSelfDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        dto.setStatusTemp(BaseImApplyStatusEnum.CANCELLED);

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

        Long currentUserId = MyUserUtil.getCurrentUserId();

        dto.setCurrentUserId(currentUserId);

        dto.setStatusTemp(BaseImApplyStatusEnum.CANCELLED);

        dto.setBaseImTypeEnum(BaseImTypeEnum.GROUP.getCode());

        Page<BaseImApplyGroupPageGroupVO> page = baseMapper.myPageGroup(dto.pageOrder(), dto);

        Set<Long> avatarFileIdSet = new HashSet<>(page.getRecords().size());

        for (BaseImApplyGroupPageGroupVO item : page.getRecords()) {

            avatarFileIdSet.add(item.getAvatarFileId());

            avatarFileIdSet.add(item.getGroupAvatarFileId());

        }

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseImApplyGroupPageGroupVO item : page.getRecords()) {

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            item.setAvatarFileId(null);

            item.setAvatarUrl(avatarUrl);

            String groupAvatarUrl = publicUrlMap.get(item.getGroupAvatarFileId());

            item.setGroupAvatarFileId(null);

            item.setGroupAvatarUrl(groupAvatarUrl);

        }

        return page;

    }

    /**
     * 同意
     */
    @Override
    @DSTransactional
    public String agree(BaseImApplyGroupAgreeDTO dto) {

        boolean batchFlag = dto.getList().size() != 1;

        BaseImGroupUtil.checkForTargetUserId(dto.getList(), (groupId, userIdSet) -> {

            RedissonUtil.doMultiLockSuf(BaseRedisKeyEnum.PRE_IM_APPLY_GROUP + ":", userIdSet, ":" + groupId, () -> {

                for (Long item : userIdSet) {

                    BaseImApplyGroupDO baseImApplyGroupDO = lambdaQuery().eq(BaseImApplyGroupDO::getUserId, item)
                        .eq(BaseImApplyGroupDO::getTargetGroupId, groupId).one();

                    if (baseImApplyGroupDO == null) {
                        if (batchFlag) {
                            continue;
                        } else {
                            R.error("操作失败：入群申请不存在", item);
                        }
                    }

                    if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyGroupDO.getStatus())) {
                        if (batchFlag) {
                            continue;
                        } else {
                            R.error("操作失败：该入群申请状态已发生改变，请刷新再试", item);
                        }
                    }

                    baseImApplyGroupDO.setRejectReason("");

                    baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.PASSED);

                    baseImApplyGroupDO.setUpdateTime(new Date());

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
                    baseImGroupRefUserService.addUser(baseImApplyGroupDO.getSessionId(),
                        baseImApplyGroupDO.getTargetGroupId(), baseImApplyGroupDO.getUserId());

                }

            });

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 拒绝
     */
    @Override
    @DSTransactional
    public String reject(BaseImApplyGroupRejectDTO dto) {

        boolean batchFlag = dto.getList().size() != 1;

        BaseImGroupUtil.checkForTargetUserId(dto.getList(), (groupId, userIdSet) -> {

            RedissonUtil.doMultiLockSuf(BaseRedisKeyEnum.PRE_IM_APPLY_GROUP + ":", userIdSet, ":" + groupId, () -> {

                for (Long item : userIdSet) {

                    BaseImApplyGroupDO baseImApplyGroupDO = lambdaQuery().eq(BaseImApplyGroupDO::getId, item)
                        .eq(BaseImApplyGroupDO::getTargetGroupId, groupId).one();

                    if (baseImApplyGroupDO == null) {
                        if (batchFlag) {
                            continue;
                        } else {
                            R.error("操作失败：入群申请不存在", item);
                        }
                    }

                    if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyGroupDO.getStatus())) {
                        if (batchFlag) {
                            continue;
                        } else {
                            R.error("操作失败：该入群申请状态已发生改变，请刷新再试", item);
                        }
                    }

                    baseImApplyGroupDO.setRejectReason(MyEntityUtil.getNotNullStr(dto.getRejectReason()));

                    baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.REJECTED);

                    baseImApplyGroupDO.setUpdateTime(new Date());

                    // 更新数据
                    updateById(baseImApplyGroupDO);

                    // 显示入群申请
                    ChainWrappers.lambdaUpdateChain(baseImApplyGroupExtraMapper)
                        .eq(BaseImApplyGroupExtraDO::getApplyGroupId, baseImApplyGroupDO.getId())
                        .set(BaseImApplyGroupExtraDO::getHiddenFlag, false).update();

                }

            });

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏-自我
     */
    @Override
    public String hiddenSelf(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        baseImApplyGroupExtraMapper.insertOrUpdateHiddenFlag(dto.getIdSet(), currentUserId, true, 101);

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏-群组
     */
    @Override
    public String hiddenGroup(BaseImApplyGroupHiddenGroupDTO dto) {

        BaseImGroupUtil.checkForTargetUserId(dto.getList(), (groupId, userIdSet) -> {

            List<BaseImApplyGroupDO> baseImApplyGroupDoList =
                lambdaQuery().eq(BaseImApplyGroupDO::getTargetGroupId, groupId)
                    .in(BaseImApplyGroupDO::getUserId, userIdSet).select(BaseImApplyGroupDO::getId).list();

            if (CollUtil.isEmpty(baseImApplyGroupDoList)) {
                return;
            }

            Set<Long> imApplyGroupIdSet =
                baseImApplyGroupDoList.stream().map(BaseImApplyGroupDO::getId).collect(Collectors.toSet());

            baseImApplyGroupExtraMapper.insertOrUpdateHiddenFlag(imApplyGroupIdSet, groupId, true, 201);

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消-自我
     */
    @Override
    public String cancelSelf(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_IM_APPLY_GROUP + ":" + currentUserId + ":", dto.getIdSet(),
            () -> {

                for (Long item : dto.getIdSet()) {

                    BaseImApplyGroupDO baseImApplyGroupDO =
                        lambdaQuery().eq(BaseImApplyGroupDO::getUserId, currentUserId)
                            .eq(BaseImApplyGroupDO::getTargetGroupId, item).one();

                    if (baseImApplyGroupDO == null) {
                        if (dto.getIdSet().size() != 1) {
                            continue;
                        } else {
                            R.error("操作失败：群聊申请不存在", item);
                        }
                    }

                    if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyGroupDO.getStatus())) {
                        if (dto.getIdSet().size() != 1) {
                            continue;
                        } else {
                            R.error("操作失败：该群聊申请状态已发生改变，请刷新再试", item);
                        }
                    }

                    baseImApplyGroupDO.setUpdateTime(new Date());

                    baseImApplyGroupDO.setStatus(BaseImApplyStatusEnum.CANCELLED);

                    // 更新数据
                    updateById(baseImApplyGroupDO);

                }

            });

        return TempBizCodeEnum.OK;

    }

}
