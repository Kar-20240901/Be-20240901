package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserQueryLastContentVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.im.BaseImSessionRefUserService;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyPageUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
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
public class BaseImSessionRefUserServiceImpl extends ServiceImpl<BaseImSessionRefUserMapper, BaseImSessionRefUserDO>
    implements BaseImSessionRefUserService {

    @Resource
    BaseUserInfoMapper baseUserInfoMapper;

    @Resource
    BaseFileService baseFileService;

    @Resource
    BaseImSessionContentRefUserMapper baseImSessionContentRefUserMapper;

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    /**
     * 创建会话关联用户：好友
     */
    @Override
    @MyTransactional
    public void addOrUpdateSessionRefUserForFriend(Long sessionId, Long userId1, Long userId2, boolean addFlag) {

        Assert.notNull(sessionId);
        Assert.notNull(userId1);
        Assert.notNull(userId2);

        List<TempUserInfoDO> tempUserInfoDOList =
            ChainWrappers.lambdaQueryChain(baseUserInfoMapper).in(TempUserInfoDO::getId, userId1, userId2)
                .select(TempUserInfoDO::getId, TempUserInfoDO::getNickname, TempUserInfoDO::getAvatarFileId).list();

        if (tempUserInfoDOList.size() != 2) {
            R.error("操作失败：用户信息不存在，请重新申请", CollUtil.newArrayList(userId1, userId2));
        }

        if (addFlag) {

            lambdaUpdate().eq(BaseImSessionRefUserDO::getSessionId, sessionId)
                .set(BaseImSessionRefUserDO::getShowFlag, true).update();

            return;

        }

        Map<Long, TempUserInfoDO> userInfoMap =
            tempUserInfoDOList.stream().collect(Collectors.toMap(TempUserInfoDO::getId, it -> it));

        Date date = new Date();

        TempUserInfoDO tempUserInfoDo1 = userInfoMap.get(userId1);

        TempUserInfoDO tempUserInfoDo2 = userInfoMap.get(userId2);

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(
            CollUtil.newHashSet(tempUserInfoDo1.getAvatarFileId(), tempUserInfoDo2.getAvatarFileId()))).getMap();

        BaseImSessionRefUserDO baseImSessionRefUserDo1 = new BaseImSessionRefUserDO();

        baseImSessionRefUserDo1.setSessionId(sessionId);
        baseImSessionRefUserDo1.setUserId(userId1);
        baseImSessionRefUserDo1.setLastOpenTs(date.getTime());
        baseImSessionRefUserDo1.setShowFlag(true);
        baseImSessionRefUserDo1.setName("");
        baseImSessionRefUserDo1.setAvatarUrl(
            MyEntityUtil.getNotNullStr(publicUrlMap.get(tempUserInfoDo2.getAvatarFileId())));
        baseImSessionRefUserDo1.setTargetId(userId2);
        baseImSessionRefUserDo1.setTargetType(BaseImTypeEnum.FRIEND.getCode());
        baseImSessionRefUserDo1.setTargetName(tempUserInfoDo2.getNickname());

        saveOrUpdate(baseImSessionRefUserDo1);

        BaseImSessionRefUserDO baseImSessionRefUserDo2 = new BaseImSessionRefUserDO();

        baseImSessionRefUserDo2.setSessionId(sessionId);
        baseImSessionRefUserDo2.setUserId(userId2);
        baseImSessionRefUserDo2.setLastOpenTs(date.getTime());
        baseImSessionRefUserDo2.setShowFlag(true);
        baseImSessionRefUserDo2.setName("");
        baseImSessionRefUserDo2.setAvatarUrl(publicUrlMap.get(tempUserInfoDo1.getAvatarFileId()));
        baseImSessionRefUserDo2.setTargetId(userId1);
        baseImSessionRefUserDo2.setTargetType(BaseImTypeEnum.FRIEND.getCode());
        baseImSessionRefUserDo1.setTargetName(tempUserInfoDo1.getNickname());

        saveOrUpdate(baseImSessionRefUserDo2);

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImSessionRefUserPageVO> myPage(BaseImSessionRefUserPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Page<BaseImSessionRefUserPageVO> resPage = baseMapper.myPage(dto.pageOrder(), dto, currentUserId);

        if (!BooleanUtil.isTrue(dto.getQueryContentFlag())) {
            return resPage;
        }

        Set<Long> sessionIdSet =
            resPage.getRecords().stream().map(BaseImSessionRefUserPageVO::getSessionId).collect(Collectors.toSet());

        Map<Long, BaseImSessionRefUserQueryLastContentVO> map = queryLastContentMap(new NotEmptyIdSet(sessionIdSet));

        for (BaseImSessionRefUserPageVO item : resPage.getRecords()) {

            BaseImSessionRefUserQueryLastContentVO baseImSessionRefUserQueryLastContentVO =
                map.get(item.getSessionId());

            if (baseImSessionRefUserQueryLastContentVO == null) {
                continue;
            }

            item.setLastContent(baseImSessionRefUserQueryLastContentVO.getLastContent());

            item.setLastContentType(baseImSessionRefUserQueryLastContentVO.getLastContentType());

            item.setLastContentCreateTime(baseImSessionRefUserQueryLastContentVO.getLastContentCreateTime());

            item.setUnReadCount(baseImSessionRefUserQueryLastContentVO.getUnReadCount());

        }

        return resPage;

    }

    /**
     * 滚动加载
     */
    @Override
    public List<BaseImSessionRefUserPageVO> scroll(ScrollListDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Long sessionId = dto.getId();

        boolean backwardFlag = BooleanUtil.isTrue(dto.getBackwardFlag());

        if (sessionId == null) {

            if (backwardFlag) { // 最小的 id

                sessionId = Long.MIN_VALUE;

            } else { // 最大的 id

                sessionId = Long.MAX_VALUE;

            }

        }

        BaseImSessionRefUserPageDTO pageDTO = new BaseImSessionRefUserPageDTO();

        pageDTO.setSessionId(sessionId);

        pageDTO.setBackwardFlag(backwardFlag);

        pageDTO.setSearchKey(dto.getSearchKey());

        Page<BaseImSessionRefUserPageVO> resPage =
            baseMapper.myPage(MyPageUtil.getScrollPage(dto.getPageSize()), pageDTO, currentUserId);

        return resPage.getRecords();

    }

    /**
     * 查询最新消息和未读消息数量
     */
    @Override
    public Map<Long, BaseImSessionRefUserQueryLastContentVO> queryLastContentMap(NotEmptyIdSet dto) {

        Set<Long> sessionIdSet = dto.getIdSet();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        List<BaseImSessionRefUserDO> baseImSessionRefUserDoList =
            lambdaQuery().in(BaseImSessionRefUserDO::getId, sessionIdSet)
                .eq(BaseImSessionRefUserDO::getUserId, currentUserId).select(BaseImSessionRefUserDO::getSessionId)
                .list();

        List<Long> sessionIdList =
            baseImSessionRefUserDoList.stream().map(BaseImSessionRefUserDO::getSessionId).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(sessionIdList)) {
            return MapUtil.newHashMap();
        }

        Map<Long, Integer> sessionIdUnReadCountMap = baseMapper.queryUnReadCount(sessionIdList, currentUserId);

        List<BaseImSessionRefUserQueryLastContentVO> baseImSessionRefUserQueryLastContentVoList =
            baseImSessionContentRefUserMapper.queryLastContent(sessionIdList, currentUserId);

        Map<Long, BaseImSessionRefUserQueryLastContentVO> resMap =
            MapUtil.newHashMap(baseImSessionRefUserQueryLastContentVoList.size());

        for (BaseImSessionRefUserQueryLastContentVO item : baseImSessionRefUserQueryLastContentVoList) {

            item.setUnReadCount(sessionIdUnReadCountMap.getOrDefault(item.getSessionId(), 0));

            resMap.put(item.getSessionId(), item);

        }

        return resMap;

    }

    /**
     * 隐藏
     */
    @Override
    public String hidden(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        lambdaUpdate().eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .eq(BaseImSessionRefUserDO::getSessionId, dto.getId()).set(BaseImSessionRefUserDO::getShowFlag, false)
            .update();

        return TempBizCodeEnum.OK;

    }

    /**
     * 更新最后一次打开会话的时间
     */
    @Override
    public String updateLastOpenTs(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        lambdaUpdate().eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .eq(BaseImSessionRefUserDO::getSessionId, dto.getId())
            .set(BaseImSessionRefUserDO::getLastOpenTs, new Date()).set(BaseImSessionRefUserDO::getShowFlag, true);

        return TempBizCodeEnum.OK;

    }

    /**
     * 更新头像和昵称
     */
    @Override
    @MyTransactional
    public String updateAvatarAndNickname(NotEmptyIdSet dto) {

        Set<Long> sessionIdSet = dto.getIdSet();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        List<BaseImSessionRefUserDO> baseImSessionRefUserDoList =
            lambdaQuery().in(BaseImSessionRefUserDO::getSessionId, sessionIdSet)
                .eq(BaseImSessionRefUserDO::getUserId, currentUserId)
                .select(BaseImSessionRefUserDO::getId, BaseImSessionRefUserDO::getTargetId,
                    BaseImSessionRefUserDO::getTargetType).list();

        if (CollUtil.isEmpty(baseImSessionRefUserDoList)) {

            return TempBizCodeEnum.OK;

        }

        List<Long> friendIdList = new ArrayList<>();

        List<Long> groupIdList = new ArrayList<>();

        for (BaseImSessionRefUserDO item : baseImSessionRefUserDoList) {

            if (BaseImTypeEnum.FRIEND.getCode() == item.getTargetType()) {

                friendIdList.add(item.getTargetId());

            } else if (BaseImTypeEnum.GROUP.getCode() == item.getTargetType()) {

                groupIdList.add(item.getTargetId());

            }

        }

        Map<Long, TempUserInfoDO> userInfoDoMap = MapUtil.newHashMap();

        Map<Long, BaseImGroupDO> groupDoMap = MapUtil.newHashMap();

        Set<Long> avatarSet = new HashSet<>();

        if (CollUtil.isNotEmpty(friendIdList)) {

            List<TempUserInfoDO> tempUserInfoDoList =
                ChainWrappers.lambdaQueryChain(baseUserInfoMapper).in(TempUserInfoDO::getId, friendIdList)
                    .select(TempUserInfoDO::getId, TempUserInfoDO::getNickname, TempUserInfoDO::getAvatarFileId).list();

            for (TempUserInfoDO item : tempUserInfoDoList) {

                userInfoDoMap.put(item.getId(), item);

                avatarSet.add(item.getAvatarFileId());

            }

        }

        if (CollUtil.isNotEmpty(groupIdList)) {

            List<BaseImGroupDO> baseImGroupDoList =
                ChainWrappers.lambdaQueryChain(baseImGroupMapper).in(BaseImGroupDO::getId, groupIdList)
                    .select(BaseImGroupDO::getId, BaseImGroupDO::getName, BaseImGroupDO::getAvatarFileId).list();

            for (BaseImGroupDO item : baseImGroupDoList) {

                groupDoMap.put(item.getId(), item);

                avatarSet.add(item.getAvatarFileId());

            }

        }

        List<BaseImSessionRefUserDO> updateList = new ArrayList<>();

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarSet)).getMap();

        // 组合数据
        for (BaseImSessionRefUserDO item : baseImSessionRefUserDoList) {

            BaseImSessionRefUserDO baseImSessionRefUserDO = new BaseImSessionRefUserDO();

            baseImSessionRefUserDO.setId(item.getId());

            Long targetId = item.getTargetId();

            if (BaseImTypeEnum.FRIEND.getCode() == item.getTargetType()) {

                TempUserInfoDO tempUserInfoDO = userInfoDoMap.get(item.getTargetId());

                if (tempUserInfoDO == null) {
                    continue;
                }

                baseImSessionRefUserDO.setAvatarUrl(
                    MyEntityUtil.getNotNullStr(publicUrlMap.get(tempUserInfoDO.getAvatarFileId())));

                baseImSessionRefUserDO.setTargetName(MyEntityUtil.getNotNullStr(tempUserInfoDO.getNickname()));

            } else if (BaseImTypeEnum.GROUP.getCode() == item.getTargetType()) {

                BaseImGroupDO baseImGroupDO = groupDoMap.get(item.getTargetId());

                if (baseImGroupDO == null) {
                    continue;
                }

                baseImSessionRefUserDO.setAvatarUrl(
                    MyEntityUtil.getNotNullStr(publicUrlMap.get(baseImGroupDO.getAvatarFileId())));

                baseImSessionRefUserDO.setTargetName(baseImGroupDO.getName());

            }

            updateList.add(baseImSessionRefUserDO);

        }

        updateBatchById(updateList);

        return TempBizCodeEnum.OK;

    }

}
