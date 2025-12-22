package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyFriendExtraMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendExtraDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyFriendDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSendDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyFriendVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImApplyFriendService;
import com.kar20240901.be.base.web.service.im.BaseImFriendService;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import com.kar20240901.be.base.web.service.im.BaseImSessionService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImApplyFriendServiceImpl extends ServiceImpl<BaseImApplyFriendMapper, BaseImApplyFriendDO>
    implements BaseImApplyFriendService {

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    BaseFileService baseFileService;

    @Resource
    BaseImBlockMapper baseImBlockMapper;

    @Resource
    BaseImApplyFriendExtraMapper baseImApplyFriendExtraMapper;

    @Resource
    BaseImFriendService baseImFriendService;

    @Resource
    BaseImSessionService baseImSessionService;

    @Resource
    BaseImSessionRefUserService baseImSessionRefUserService;

    /**
     * 搜索要添加的好友
     */
    @Override
    public Page<BaseImApplyFriendSearchApplyFriendVO> searchApplyFriend(BaseImApplyFriendSearchApplyFriendDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        String searchKey = dto.getSearchKey();

        Page<BaseImApplyFriendSearchApplyFriendVO> resPage = new Page<>();

        List<BaseImFriendDO> baseImFriendDOList =
            baseImFriendService.lambdaQuery().eq(BaseImFriendDO::getBelongId, currentUserId)
                .select(BaseImFriendDO::getFriendId).list();

        Set<Long> notIdIdSet = baseImFriendDOList.stream().map(BaseImFriendDO::getFriendId).collect(Collectors.toSet());

        List<BaseImBlockDO> baseImBlockDOList =
            ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, currentUserId)
                .eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.FRIEND).select(BaseImBlockDO::getUserId).list();

        for (BaseImBlockDO item : baseImBlockDOList) {
            notIdIdSet.add(item.getUserId());
        }

        notIdIdSet.add(currentUserId);

        Page<TempUserInfoDO> page = ChainWrappers.lambdaQueryChain(baseUserInfoMapper).or(StrUtil.isNotBlank(searchKey),
                i -> i.like(TempUserInfoDO::getNickname, searchKey).or().eq(TempUserInfoDO::getUuid, searchKey)) //
            .notIn(TempUserInfoDO::getId, notIdIdSet) //
            .select(TempUserInfoDO::getId, TempUserInfoDO::getNickname, TempUserInfoDO::getAvatarFileId,
                TempUserInfoDO::getUuid, TempUserInfoDO::getBio)
            .page(dto.fieldDescDefaultOrderPage("lastActiveTime", true));

        Set<Long> avatarFileIdSet = page.getRecords().stream().map(TempUserInfoDO::getAvatarFileId)
            .filter(it -> it != TempConstant.NEGATIVE_ONE).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        List<BaseImApplyFriendSearchApplyFriendVO> list = new ArrayList<>();

        for (TempUserInfoDO item : page.getRecords()) {

            BaseImApplyFriendSearchApplyFriendVO baseImApplyFriendSearchApplyFriendVO =
                new BaseImApplyFriendSearchApplyFriendVO();

            baseImApplyFriendSearchApplyFriendVO.setUserId(item.getId());

            baseImApplyFriendSearchApplyFriendVO.setNickname(item.getNickname());

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            baseImApplyFriendSearchApplyFriendVO.setAvatarUrl(avatarUrl);

            baseImApplyFriendSearchApplyFriendVO.setUuid(item.getUuid());

            baseImApplyFriendSearchApplyFriendVO.setBio(item.getBio());

            list.add(baseImApplyFriendSearchApplyFriendVO);

        }

        resPage.setTotal(page.getTotal());

        resPage.setRecords(list);

        return resPage;

    }

    /**
     * 发送好友申请
     */
    @Override
    @DSTransactional
    public String send(BaseImApplyFriendSendDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getIdSet().contains(currentUserId)) {
            R.error("操作失败：不能给自己发送好友申请", dto.getIdSet());
        }

        Long count =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).in(TempUserInfoDO::getId, dto.getIdSet()).count();

        boolean singleFlag = dto.getIdSet().size() == 1;

        if (count != dto.getIdSet().size()) {
            if (singleFlag) {
                R.error("操作失败：该用户不存在", dto.getIdSet());
            } else {
                R.error("操作失败：用户不存在，请刷新", dto.getIdSet());
            }
        }

        boolean existsBlock =
            ChainWrappers.lambdaQueryChain(baseImBlockMapper).in(BaseImBlockDO::getUserId, dto.getIdSet())
                .eq(BaseImBlockDO::getSourceId, currentUserId).eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.FRIEND)
                .exists();

        if (existsBlock) {
            if (singleFlag) {
                R.error("操作失败：您已被对方拉黑，无法添加", dto.getIdSet());
            } else {
                R.error("操作失败：您已被对方拉黑，无法添加", dto.getIdSet());
            }
        }

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND + ":" + currentUserId + ":", dto.getIdSet(),
            () -> {

                for (Long item : dto.getIdSet()) {

                    BaseImApplyFriendDO baseImApplyFriendDO =
                        lambdaQuery().eq(BaseImApplyFriendDO::getUserId, currentUserId)
                            .eq(BaseImApplyFriendDO::getTargetUserId, item).one();

                    if (baseImApplyFriendDO == null) {

                        baseImApplyFriendDO = new BaseImApplyFriendDO();

                        baseImApplyFriendDO.setUserId(currentUserId);
                        baseImApplyFriendDO.setTargetUserId(item);
                        baseImApplyFriendDO.setSessionId(TempConstant.NEGATIVE_ONE);

                    } else {

                        BaseImApplyStatusEnum baseImApplyStatusEnum = baseImApplyFriendDO.getStatus();

                        if (BaseImApplyStatusEnum.PASSED.equals(baseImApplyStatusEnum)) {

                            // 查询：是否存在于好友列表里面
                            boolean existsFriend =
                                baseImFriendService.lambdaQuery().eq(BaseImFriendDO::getBelongId, currentUserId)
                                    .eq(BaseImFriendDO::getFriendId, item).exists();

                            if (existsFriend) {
                                if (singleFlag) {
                                    R.error("操作失败：对方已经是您的好友了", item);
                                } else {
                                    continue;
                                }
                            }

                        }

                        // 显示好友申请
                        ChainWrappers.lambdaUpdateChain(baseImApplyFriendExtraMapper)
                            .eq(BaseImApplyFriendExtraDO::getApplyFriendId, baseImApplyFriendDO.getId())
                            .set(BaseImApplyFriendExtraDO::getHiddenFlag, false).update();

                    }

                    baseImApplyFriendDO.setStatus(BaseImApplyStatusEnum.APPLYING);
                    baseImApplyFriendDO.setRejectReason("");
                    baseImApplyFriendDO.setApplyTime(new Date());
                    baseImApplyFriendDO.setApplyContent(dto.getApplyContent());

                    saveOrUpdate(baseImApplyFriendDO);

                    // 通知：您有新的好友申请

                }

            });

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImApplyFriendPageVO> myPage(BaseImApplyFriendPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        dto.setToMeFlag(!BooleanUtil.isFalse(dto.getToMeFlag()));

        if (dto.getToMeFlag()) {
            dto.setStatusTemp(BaseImApplyStatusEnum.CANCELLED);
        }

        Page<BaseImApplyFriendPageVO> page = baseMapper.myPage(dto.pageOrder(), dto, currentUserId);

        Set<Long> avatarFileIdSet =
            page.getRecords().stream().map(BaseImApplyFriendPageVO::getAvatarFileId).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseImApplyFriendPageVO item : page.getRecords()) {

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
    public String agree(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND_ID + ":", dto.getIdSet(), () -> {

            for (Long item : dto.getIdSet()) {

                BaseImApplyFriendDO baseImApplyFriendDO =
                    lambdaQuery().eq(BaseImApplyFriendDO::getTargetUserId, currentUserId)
                        .eq(BaseImApplyFriendDO::getId, item).one();

                if (baseImApplyFriendDO == null) {
                    if (dto.getIdSet().size() != 1) {
                        continue;
                    } else {
                        R.error("操作失败：好友申请不存在", item);
                    }
                }

                if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyFriendDO.getStatus())) {
                    if (dto.getIdSet().size() != 1) {
                        continue;
                    } else {
                        R.error("操作失败：该好友申请状态已发生改变，请刷新再试", item);
                    }
                }

                Long sessionId = baseImApplyFriendDO.getSessionId();

                // 防止会话记录丢失，则采用历史的会话主键 id
                boolean addFlag = sessionId.equals(TempConstant.NEGATIVE_ONE);

                if (addFlag) {

                    sessionId = IdGeneratorUtil.nextId();

                }

                // 更新数据：两个申请同时更新
                lambdaUpdate().eq(BaseImApplyFriendDO::getUserId, baseImApplyFriendDO.getTargetUserId())
                    .eq(BaseImApplyFriendDO::getTargetUserId, currentUserId)
                    .or(i -> i.eq(BaseImApplyFriendDO::getUserId, currentUserId)
                        .eq(BaseImApplyFriendDO::getTargetUserId, baseImApplyFriendDO.getTargetUserId()))
                    .set(BaseImApplyFriendDO::getStatus, BaseImApplyStatusEnum.PASSED)
                    .set(BaseImApplyFriendDO::getRejectReason, "").set(BaseImApplyFriendDO::getSessionId, sessionId)
                    .update();

                // 显示好友申请：两个申请同时显示
                ChainWrappers.lambdaUpdateChain(baseImApplyFriendExtraMapper)
                    .eq(BaseImApplyFriendExtraDO::getApplyFriendId, baseImApplyFriendDO.getId())
                    .set(BaseImApplyFriendExtraDO::getHiddenFlag, false).update();

                // 创建好友
                baseImFriendService.addOrUpdateFriend(baseImApplyFriendDO.getUserId(),
                    baseImApplyFriendDO.getTargetUserId(), sessionId, addFlag);

                if (addFlag) {

                    // 创建会话
                    baseImSessionService.addSession(sessionId, baseImApplyFriendDO.getId(), BaseImTypeEnum.FRIEND);

                }

                // 创建会话关联用户
                baseImSessionRefUserService.addOrUpdateSessionRefUserForFriend(sessionId,
                    baseImApplyFriendDO.getUserId(), baseImApplyFriendDO.getTargetUserId(), addFlag);

            }

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 拒绝
     */
    @Override
    @DSTransactional
    public String reject(BaseImApplyFriendRejectDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND_ID + ":", dto.getIdSet(), () -> {

            for (Long item : dto.getIdSet()) {

                BaseImApplyFriendDO baseImApplyFriendDO =
                    lambdaQuery().eq(BaseImApplyFriendDO::getTargetUserId, currentUserId)
                        .eq(BaseImApplyFriendDO::getId, item).one();

                if (baseImApplyFriendDO == null) {
                    if (dto.getIdSet().size() != 1) {
                        continue;
                    } else {
                        R.error("操作失败：好友申请不存在", item);
                    }
                }

                if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyFriendDO.getStatus())) {
                    if (dto.getIdSet().size() != 1) {
                        continue;
                    } else {
                        R.error("操作失败：该好友申请状态已发生改变，请刷新再试", item);
                    }
                }

                baseImApplyFriendDO.setRejectReason(MyEntityUtil.getNotNullStr(dto.getRejectReason()));

                baseImApplyFriendDO.setStatus(BaseImApplyStatusEnum.REJECTED);

                // 更新数据
                updateById(baseImApplyFriendDO);

                // 显示好友申请
                ChainWrappers.lambdaUpdateChain(baseImApplyFriendExtraMapper)
                    .eq(BaseImApplyFriendExtraDO::getApplyFriendId, baseImApplyFriendDO.getId())
                    .set(BaseImApplyFriendExtraDO::getHiddenFlag, false).update();

            }

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏
     */
    @Override
    public String hidden(NotEmptyIdSet dto) {

        Long userId = MyUserUtil.getCurrentUserId();

        Long count = lambdaQuery().and(i -> i.eq(BaseImApplyFriendDO::getTargetUserId, userId)
                .or(o -> o.eq(BaseImApplyFriendDO::getUserId, userId))).in(BaseImApplyFriendDO::getId, dto.getIdSet())
            .count();

        if (count != dto.getIdSet().size()) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        baseImApplyFriendExtraMapper.insertOrUpdateHiddenFlag(dto.getIdSet(), userId, true);

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消
     */
    @Override
    @DSTransactional
    public String cancel(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        RedissonUtil.doMultiLock(BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND_ID + ":", dto.getIdSet(), () -> {

            for (Long item : dto.getIdSet()) {

                BaseImApplyFriendDO baseImApplyFriendDO =
                    lambdaQuery().eq(BaseImApplyFriendDO::getUserId, currentUserId).eq(BaseImApplyFriendDO::getId, item)
                        .one();

                if (baseImApplyFriendDO == null) {
                    if (dto.getIdSet().size() != 1) {
                        continue;
                    } else {
                        R.error("操作失败：好友申请不存在", item);
                    }
                }

                if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyFriendDO.getStatus())) {
                    if (dto.getIdSet().size() != 1) {
                        continue;
                    } else {
                        R.error("操作失败：该好友申请状态已发生改变，请刷新再试", item);
                    }
                }

                baseImApplyFriendDO.setStatus(BaseImApplyStatusEnum.CANCELLED);

                // 更新数据
                updateById(baseImApplyFriendDO);

            }

        });

        return TempBizCodeEnum.OK;

    }

}
