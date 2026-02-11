package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionContentRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionContentRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionContentRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionContentRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImSessionContentRefUserService;
import com.kar20240901.be.base.web.util.base.MyPageUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionContentRefUserServiceImpl
    extends ServiceImpl<BaseImSessionContentRefUserMapper, BaseImSessionContentRefUserDO>
    implements BaseImSessionContentRefUserService {

    private static BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    @Resource
    public void setBaseImSessionRefUserMapper(BaseImSessionRefUserMapper baseImSessionRefUserMapper) {
        BaseImSessionContentRefUserServiceImpl.baseImSessionRefUserMapper = baseImSessionRefUserMapper;
    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseImSessionContentRefUserPageVO> myPage(BaseImSessionContentRefUserPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return baseMapper.myPage(dto.pageOrder(), dto, currentUserId);

    }

    /**
     * 滚动加载
     */
    @Override
    public List<BaseImSessionContentRefUserPageVO> scroll(ScrollListDTO dto) {

        Assert.notNull(dto.getRefId());

        Long currentUserId = MyUserUtil.getCurrentUserId();

        // 默认是：更新
        if (BooleanUtil.isFalse(dto.getBoolean1())) {
            updateLastOpenTs(currentUserId, dto.getRefId()); // 更新最后一次打开会话的时间
        }

        boolean backwardFlag = BooleanUtil.isTrue(dto.getBackwardFlag());

        // 获取：滚动加载时的 id
        Long contentId = MyPageUtil.getScrollId(dto);

        BaseImSessionContentRefUserPageDTO pageDTO = new BaseImSessionContentRefUserPageDTO();

        pageDTO.setBackwardFlag(backwardFlag);

        pageDTO.setContentId(contentId);

        pageDTO.setContent(dto.getSearchKey());

        pageDTO.setSessionId(dto.getRefId());

        List<BaseImSessionContentRefUserPageVO> records =
            baseMapper.myPage(MyPageUtil.getScrollPage(dto.getPageSize()), pageDTO, currentUserId).getRecords();

        // 后续处理：records
        return scrollHandleRecords(dto, records, backwardFlag, pageDTO, currentUserId);

    }

    /**
     * 后续处理：records
     */
    private List<BaseImSessionContentRefUserPageVO> scrollHandleRecords(ScrollListDTO dto,
        List<BaseImSessionContentRefUserPageVO> records, boolean backwardFlag,
        BaseImSessionContentRefUserPageDTO pageDTO, Long currentUserId) {

        if (!BooleanUtil.isTrue(dto.getQueryMoreFlag())) {
            return records;
        }

        if (CollUtil.isEmpty(records)) {
            return records;
        }

        long pageSize;

        long morePageSize = 2L;

        if (records.size() < dto.getPageSize()) {

            pageSize = dto.getPageSize() + morePageSize - records.size();

        } else {

            pageSize = morePageSize;

        }

        pageDTO.setBackwardFlag(!backwardFlag);

        List<BaseImSessionContentRefUserPageVO> moreRecords =
            baseMapper.myPage(MyPageUtil.getScrollPage(pageSize), pageDTO, currentUserId).getRecords();

        if (CollUtil.isEmpty(moreRecords)) {
            return records;
        }

        if (backwardFlag) {

            CollUtil.addAll(moreRecords, records);

            return moreRecords;

        } else {

            CollUtil.addAll(records, moreRecords);

            return records;

        }

    }

    /**
     * 更新最后一次打开会话的时间
     */
    public static void updateLastOpenTs(Long userId, Long sessionId) {

        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper).eq(BaseImSessionRefUserDO::getUserId, userId)
            .eq(BaseImSessionRefUserDO::getSessionId, sessionId)
            .set(BaseImSessionRefUserDO::getLastOpenTs, new Date().getTime())
            .set(BaseImSessionRefUserDO::getShowFlag, true).update();

    }

    /**
     * 清空聊天记录
     */
    @Override
    public String deleteSessionContentRefUser(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Set<Long> sessionIdSet = dto.getIdSet();

        lambdaUpdate().eq(BaseImSessionContentRefUserDO::getUserId, currentUserId)
            .in(BaseImSessionContentRefUserDO::getSessionId, sessionIdSet).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 隐藏消息内容
     */
    @Override
    public String hideSessionContentRefUser(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Set<Long> contentIdSet = dto.getIdSet();

        lambdaUpdate().eq(BaseImSessionContentRefUserDO::getUserId, currentUserId)
            .in(BaseImSessionContentRefUserDO::getContentId, contentIdSet)
            .set(BaseImSessionContentRefUserDO::getShowFlag, false).update();

        return TempBizCodeEnum.OK;

    }

}
