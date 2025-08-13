package com.kar20240901.be.base.web.service.im.impl;

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
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
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

        String nickname = dto.getNickname();

        Long userId = dto.getUserId();

        Page<BaseImApplyFriendSearchApplyFriendVO> resPage = new Page<>();

        if (StrUtil.isBlank(nickname) && userId == null) {
            return resPage;
        }

        Page<TempUserInfoDO> page =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).eq(userId != null, TempUserInfoDO::getId, userId)
                .like(StrUtil.isNotBlank(nickname), TempUserInfoDO::getNickname, nickname)
                .select(TempUserInfoDO::getId, TempUserInfoDO::getNickname, TempUserInfoDO::getAvatarFileId)
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

        if (dto.getId().equals(currentUserId)) {
            R.error("操作失败：不能给自己发送好友申请", dto.getId());
        }

        boolean exists =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).eq(TempUserInfoDO::getId, dto.getId()).exists();

        if (!exists) {
            R.error("操作失败：该用户不存在", dto.getId());
        }

        boolean existsBlock =
            ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getUserId, dto.getId())
                .eq(BaseImBlockDO::getSourceId, currentUserId).eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.FRIEND)
                .exists();

        if (existsBlock) {
            R.error("操作失败：您已被对方拉黑，无法添加", dto.getId());
        }

        String lockKey = BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND + ":" + currentUserId + ":" + dto.getId();

        RedissonUtil.doLock(lockKey, () -> {

            BaseImApplyFriendDO baseImApplyFriendDO = lambdaQuery().eq(BaseImApplyFriendDO::getUserId, currentUserId)
                .eq(BaseImApplyFriendDO::getTargetUserId, dto.getId()).one();

            if (baseImApplyFriendDO == null) {

                baseImApplyFriendDO = new BaseImApplyFriendDO();

                baseImApplyFriendDO.setUserId(currentUserId);
                baseImApplyFriendDO.setTargetUserId(dto.getId());
                baseImApplyFriendDO.setSessionId(TempConstant.NEGATIVE_ONE);

            } else {

                BaseImApplyStatusEnum baseImApplyStatusEnum = baseImApplyFriendDO.getStatus();

                if (BaseImApplyStatusEnum.PASSED.equals(baseImApplyStatusEnum)) {

                    // 查询：是否存在于好友列表里面
                    boolean existsFriend =
                        baseImFriendService.lambdaQuery().eq(BaseImFriendDO::getBelongId, currentUserId)
                            .eq(BaseImFriendDO::getFriendId, dto.getId()).exists();

                    if (existsFriend) {

                        R.error("操作失败：对方已经是您的好友了", dto.getId());

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

        });

        // 通知：您有新的好友申请

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImApplyFriendPageVO> myPage(BaseImApplyFriendPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

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
    public String agree(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        String lockKey = BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND_ID + ":" + dto.getId();

        RedissonUtil.doLock(lockKey, () -> {

            BaseImApplyFriendDO baseImApplyFriendDO =
                lambdaQuery().eq(BaseImApplyFriendDO::getTargetUserId, currentUserId)
                    .eq(BaseImApplyFriendDO::getId, dto.getId()).one();

            if (baseImApplyFriendDO == null) {
                R.error("操作失败：好友申请不存在", dto.getId());
            }

            if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyFriendDO.getStatus())) {
                R.error("操作失败：该好友申请状态已发生改变，请刷新再试", dto.getId());
            }

            baseImApplyFriendDO.setRejectReason("");

            baseImApplyFriendDO.setStatus(BaseImApplyStatusEnum.PASSED);

            Long sessionId = baseImApplyFriendDO.getSessionId();

            // 防止会话记录丢失，则采用历史的会话主键 id
            boolean addFlag = sessionId.equals(TempConstant.NEGATIVE_ONE);

            if (addFlag) {

                sessionId = IdGeneratorUtil.nextId();

            }

            baseImApplyFriendDO.setSessionId(sessionId);

            // 更新数据
            updateById(baseImApplyFriendDO);

            // 显示好友申请
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
            baseImSessionRefUserService.addOrUpdateSessionRefUserForFriend(sessionId, baseImApplyFriendDO.getUserId(),
                baseImApplyFriendDO.getTargetUserId(), addFlag);

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

        String lockKey = BaseRedisKeyEnum.PRE_IM_APPLY_FRIEND_ID + ":" + dto.getId();

        RedissonUtil.doLock(lockKey, () -> {

            BaseImApplyFriendDO baseImApplyFriendDO =
                lambdaQuery().eq(BaseImApplyFriendDO::getTargetUserId, currentUserId)
                    .eq(BaseImApplyFriendDO::getId, dto.getId()).one();

            if (baseImApplyFriendDO == null) {
                R.error("操作失败：好友申请不存在", dto.getId());
            }

            if (!BaseImApplyStatusEnum.APPLYING.equals(baseImApplyFriendDO.getStatus())) {
                R.error("操作失败：该好友申请状态已发生改变，请刷新再试", dto.getId());
            }

            baseImApplyFriendDO.setRejectReason(MyEntityUtil.getNotNullStr(dto.getRejectReason()));

            baseImApplyFriendDO.setStatus(BaseImApplyStatusEnum.REJECTED);

            // 更新数据
            updateById(baseImApplyFriendDO);

            // 显示好友申请
            ChainWrappers.lambdaUpdateChain(baseImApplyFriendExtraMapper)
                .eq(BaseImApplyFriendExtraDO::getApplyFriendId, baseImApplyFriendDO.getId())
                .set(BaseImApplyFriendExtraDO::getHiddenFlag, false).update();

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏
     */
    @Override
    public String hidden(NotNullId dto) {

        Long userId = MyUserUtil.getCurrentUserId();

        boolean exists = lambdaQuery().and(i -> i.eq(BaseImApplyFriendDO::getTargetUserId, userId)
                .or(o -> o.eq(BaseImApplyFriendDO::getUserId, userId))).eq(BaseImApplyFriendDO::getId, dto.getId())
            .exists();

        if (!exists) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        baseImApplyFriendExtraMapper.insertOrUpdateHiddenFlag(dto.getId(), userId, true);

        return TempBizCodeEnum.OK;

    }

}
