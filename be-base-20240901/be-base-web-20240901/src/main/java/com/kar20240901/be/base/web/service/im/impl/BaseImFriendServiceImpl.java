package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImFriendMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImFriendPageDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.im.BaseImFriendPageVO;
import com.kar20240901.be.base.web.service.im.BaseImFriendService;
import com.kar20240901.be.base.web.util.base.MyPageUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.im.BaseImBlockUtil;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImFriendServiceImpl extends ServiceImpl<BaseImFriendMapper, BaseImFriendDO>
    implements BaseImFriendService {

    @Resource
    BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    /**
     * 添加好友
     *
     * @param userId1 和 userId2 可以随便传
     */
    @DSTransactional
    @Override
    public void addOrUpdateFriend(Long userId1, Long userId2, Long sessionId, boolean addFlag) {

        Set<Long> belongIdSet = CollUtil.newHashSet();

        // 支持：单向好友删除
        if (!addFlag) {

            List<BaseImFriendDO> baseImFriendDoList =
                lambdaQuery().eq(BaseImFriendDO::getSessionId, sessionId).select(BaseImFriendDO::getBelongId).list();

            belongIdSet = baseImFriendDoList.stream().map(BaseImFriendDO::getBelongId).collect(Collectors.toSet());

        }

        Date date = new Date();

        if (!belongIdSet.contains(userId1)) {

            BaseImFriendDO baseImFriendDo1 = new BaseImFriendDO();

            baseImFriendDo1.setCreateTime(date);
            baseImFriendDo1.setBelongId(userId1);
            baseImFriendDo1.setFriendId(userId2);
            baseImFriendDo1.setSessionId(sessionId);

            save(baseImFriendDo1);

        }

        if (!belongIdSet.contains(userId2)) {

            BaseImFriendDO baseImFriendDo2 = new BaseImFriendDO();

            baseImFriendDo2.setCreateTime(date);
            baseImFriendDo2.setBelongId(userId2);
            baseImFriendDo2.setFriendId(userId1);
            baseImFriendDo2.setSessionId(sessionId);

            save(baseImFriendDo2);

        }

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImFriendPageVO> myPage(BaseImFriendPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Page<BaseImFriendPageVO> page = baseMapper.myPage(dto.pageOrder(), dto, currentUserId);

        if (BooleanUtil.isTrue(dto.getQueryBlockFlag())) {

            // 处理：拉黑情况
            myPageHandleBlock(page, currentUserId);

        }

        return page;

    }

    /**
     * 处理：拉黑情况
     */
    private static void myPageHandleBlock(Page<BaseImFriendPageVO> page, Long currentUserId) {

        if (CollUtil.isEmpty(page.getRecords())) {
            return;
        }

        List<Long> userIdList =
            page.getRecords().stream().map(BaseImFriendPageVO::getFriendUserId).collect(Collectors.toList());

        Set<Long> blockUserIdSet = BaseImBlockUtil.getBlockUserIdSet(currentUserId, userIdList);

        if (CollUtil.isEmpty(blockUserIdSet)) {
            return;
        }

        for (BaseImFriendPageVO item : page.getRecords()) {

            item.setBlockFlag(blockUserIdSet.contains(item.getFriendUserId()));

        }

    }

    /**
     * 滚动加载
     */
    @Override
    public List<BaseImFriendPageVO> scroll(ScrollListDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean backwardFlag = BooleanUtil.isTrue(dto.getBackwardFlag());

        // 获取：滚动加载时的 id
        Long imFriendId = MyPageUtil.getScrollId(dto);

        BaseImFriendPageDTO pageDTO = new BaseImFriendPageDTO();

        pageDTO.setImFriendId(imFriendId);

        pageDTO.setBackwardFlag(backwardFlag);

        pageDTO.setSearchKey(dto.getSearchKey());

        Page<BaseImFriendPageVO> resPage =
            baseMapper.myPage(MyPageUtil.getScrollPage(dto.getPageSize()), pageDTO, currentUserId);

        return resPage.getRecords();

    }

    /**
     * 删除好友
     */
    @Override
    public String removeFriend(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Set<Long> friendUserIdSet = dto.getIdSet();

        lambdaUpdate().eq(BaseImFriendDO::getBelongId, currentUserId).in(BaseImFriendDO::getFriendId, friendUserIdSet)
            .remove();

        // 隐藏会话，注意：不能删除会话
        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper)
            .in(BaseImSessionRefUserDO::getTargetId, friendUserIdSet)
            .eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .eq(BaseImSessionRefUserDO::getTargetType, BaseImTypeEnum.FRIEND)
            .set(BaseImSessionRefUserDO::getShowFlag, false).update();

        return TempBizCodeEnum.OK;

    }

}
