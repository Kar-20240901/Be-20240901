package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSearchHistoryMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSearchHistoryDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImFriendPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchBaseDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchHistoryPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImFriendPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseContentVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseFriendVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchHistoryVO;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
import com.kar20240901.be.base.web.service.im.BaseImSearchService;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class BaseImSearchServiceImpl implements BaseImSearchService {

    @Resource
    BaseImSearchHistoryMapper baseImSearchHistoryMapper;

    @Resource
    BaseImFriendMapper baseImFriendMapper;

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    @Resource
    BaseImSessionContentRefUserMapper baseImSessionContentRefUserMapper;

    @Resource
    BaseImGroupService baseImGroupService;

    /**
     * 搜索历史-分页排序查询
     */
    @Override
    public Page<BaseImSearchHistoryVO> searchHistoryPage(BaseImSearchHistoryPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return baseImSearchHistoryMapper.searchHistoryPage(dto.page(), dto, currentUserId);

    }

    /**
     * 搜索历史-删除
     */
    @Override
    public String searchHistoryDelete(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        ChainWrappers.lambdaUpdateChain(baseImSearchHistoryMapper).eq(BaseImSearchHistoryDO::getBelongId, currentUserId)
            .eq(BaseImSearchHistoryDO::getId, dto.getId()).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 搜索历史-删除所有
     */
    @Override
    public String searchHistoryDeleteAll() {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        ChainWrappers.lambdaUpdateChain(baseImSearchHistoryMapper).eq(BaseImSearchHistoryDO::getBelongId, currentUserId)
            .remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 搜索：联系人，群聊，聊天记录
     */
    @SneakyThrows
    @Override
    public BaseImSearchBaseVO searchBase(BaseImSearchBaseDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        String searchKey = dto.getSearchKey();

        // 先移除：重复的搜索内容
        ChainWrappers.lambdaUpdateChain(baseImSearchHistoryMapper)
            .eq(BaseImSearchHistoryDO::getSearchHistory, searchKey)
            .eq(BaseImSearchHistoryDO::getBelongId, currentUserId).remove();

        // 再新增：新的搜索内容
        BaseImSearchHistoryDO baseImSearchHistoryDO = new BaseImSearchHistoryDO();

        baseImSearchHistoryDO.setBelongId(currentUserId);
        baseImSearchHistoryDO.setSearchHistory(searchKey);

        baseImSearchHistoryMapper.insert(baseImSearchHistoryDO);

        List<BaseImSearchBaseFriendVO> friendList = new ArrayList<>();

        List<BaseImSearchBaseGroupVO> groupList = new ArrayList<>();

        List<BaseImSearchBaseContentVO> contentList = new ArrayList<>();

        boolean searchFriendFlag = !BooleanUtil.isFalse(dto.getSearchFriendFlag());
        boolean searchGroupFlag = !BooleanUtil.isFalse(dto.getSearchGroupFlag());
        boolean searchContentFlag = !BooleanUtil.isFalse(dto.getSearchContentFlag());

        int threadCount = 0;

        if (searchFriendFlag) {
            threadCount = threadCount + 1;
        }

        if (searchGroupFlag) {
            threadCount = threadCount + 1;
        }

        if (searchContentFlag) {
            threadCount = threadCount + 1;
        }

        if (threadCount == 0) {
            return new BaseImSearchBaseVO();
        }

        Page<?> page = new Page<>();

        if (threadCount == 1) {
            page.setSize(-1);
        }

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(threadCount);

        // 查询：联系人
        if (searchFriendFlag) {
            MyThreadUtil.execute(() -> {

                BaseImFriendPageDTO baseImFriendPageDTO = new BaseImFriendPageDTO();

                baseImFriendPageDTO.setSearchKey(searchKey);

                Page<BaseImFriendPageVO> baseImFriendPageVoPage =
                    baseImFriendMapper.myPage(page, baseImFriendPageDTO, currentUserId);

                for (BaseImFriendPageVO item : baseImFriendPageVoPage.getRecords()) {

                    BaseImSearchBaseFriendVO baseImSearchBaseFriendVO = new BaseImSearchBaseFriendVO();

                    baseImSearchBaseFriendVO.setFriendUserId(item.getFriendUserId());
                    baseImSearchBaseFriendVO.setFriendShowId(item.getFriendShowId());
                    baseImSearchBaseFriendVO.setFriendShowName(item.getFriendShowName());
                    baseImSearchBaseFriendVO.setAvatarUrl(item.getAvatarUrl());
                    baseImSearchBaseFriendVO.setSessionId(item.getSessionId());

                    friendList.add(baseImSearchBaseFriendVO);

                }

            }, countDownLatch);
        }

        // 查询：群聊
        if (searchGroupFlag) {
            MyThreadUtil.execute(() -> {

                BaseImGroupPageDTO baseImGroupPageDTO = new BaseImGroupPageDTO();

                baseImGroupPageDTO.setSearchKey(searchKey);

                Page<BaseImGroupPageVO> baseImGroupPageVoPage =
                    baseImGroupMapper.myPage(page, baseImGroupPageDTO, currentUserId);

                baseImGroupService.setAvatarUrl(baseImGroupPageVoPage.getRecords(), item -> {

                    BaseImSearchBaseGroupVO baseImSearchBaseGroupVO = new BaseImSearchBaseGroupVO();

                    baseImSearchBaseGroupVO.setGroupId(item.getGroupId());
                    baseImSearchBaseGroupVO.setGroupShowId(item.getGroupShowId());
                    baseImSearchBaseGroupVO.setGroupShowName(item.getGroupShowName());
                    baseImSearchBaseGroupVO.setSessionId(item.getSessionId());

                    baseImSearchBaseGroupVO.setAvatarUrl(item.getAvatarUrl());

                    groupList.add(baseImSearchBaseGroupVO);

                });

            }, countDownLatch);
        }

        // 查询：聊天记录
        if (searchContentFlag) {
            MyThreadUtil.execute(() -> {

                BaseImSessionContentRefUserPageDTO baseImSessionContentRefUserPageDTO =
                    new BaseImSessionContentRefUserPageDTO();

                baseImSessionContentRefUserPageDTO.setContent(searchKey);

                Page<BaseImSearchBaseContentVO> baseImSearchBaseContentVoPage =
                    baseImSessionContentRefUserMapper.searchPage(page, baseImSessionContentRefUserPageDTO,
                        currentUserId);

                contentList.addAll(baseImSearchBaseContentVoPage.getRecords());

            }, countDownLatch);
        }

        countDownLatch.await();

        BaseImSearchBaseVO baseImSearchBaseVO = new BaseImSearchBaseVO();

        baseImSearchBaseVO.setFriendList(friendList);
        baseImSearchBaseVO.setGroupList(groupList);
        baseImSearchBaseVO.setContentList(contentList);

        return baseImSearchBaseVO;

    }

}
