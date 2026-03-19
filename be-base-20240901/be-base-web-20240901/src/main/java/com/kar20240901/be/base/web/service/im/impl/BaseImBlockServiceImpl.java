package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupAddUserDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImBlockService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.im.BaseImGroupUtil;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImBlockServiceImpl extends ServiceImpl<BaseImBlockMapper, BaseImBlockDO>
    implements BaseImBlockService {

    @Resource
    BaseImGroupMapper baseImGroupMapper;

    /**
     * 拉黑好友
     */
    @Override
    public String addFriend(NotEmptyIdSet dto) {

        Set<Long> userIdSet = dto.getIdSet();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (userIdSet.contains(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        List<BaseImBlockDO> baseImBlockDoList =
            lambdaQuery().eq(BaseImBlockDO::getSourceId, currentUserId).in(BaseImBlockDO::getUserId, userIdSet)
                .select(BaseImBlockDO::getUserId).list();

        if (CollUtil.isNotEmpty(baseImBlockDoList)) {

            Set<Long> blockExistUserIdSet =
                baseImBlockDoList.stream().map(BaseImBlockDO::getUserId).collect(Collectors.toSet());

            userIdSet.removeAll(blockExistUserIdSet);

        }

        if (CollUtil.isEmpty(userIdSet)) {
            return TempBizCodeEnum.OK;
        }

        baseMapper.insertOrUpdateForCreate(userIdSet, currentUserId, new Date(), currentUserId,
            BaseImTypeEnum.FRIEND.getCode());

        return TempBizCodeEnum.OK;

    }

    /**
     * 群组拉黑用户
     */
    @Override
    public String groupAddUser(BaseImBlockGroupAddUserDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        Date date = new Date();

        BaseImGroupUtil.checkForTargetUserId(dto.getList(), (groupId, userIdSet) -> {

            BaseImGroupDO baseImGroupDO =
                ChainWrappers.lambdaQueryChain(baseImGroupMapper).eq(BaseImGroupDO::getId, groupId)
                    .select(BaseImGroupDO::getSessionId).one();

            if (baseImGroupDO == null) {
                R.error(TempBizCodeEnum.INSUFFICIENT_PERMISSIONS);
            }

            baseMapper.insertOrUpdateForCreate(userIdSet, currentUserId, date, groupId, BaseImTypeEnum.GROUP.getCode());

        });

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消拉黑好友
     */
    @Override
    public String cancelFriend(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (dto.getIdSet().contains(currentUserId)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        lambdaUpdate().in(BaseImBlockDO::getUserId, dto.getIdSet()).eq(BaseImBlockDO::getSourceId, currentUserId)
            .eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.FRIEND).remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 取消群组拉黑用户
     */
    @Override
    public String groupCancelUser(BaseImBlockGroupAddUserDTO dto) {

        BaseImGroupUtil.checkForTargetUserId(dto.getList(), (groupId, userIdSet) -> {

            lambdaUpdate().in(BaseImBlockDO::getUserId, userIdSet).eq(BaseImBlockDO::getSourceId, groupId)
                .eq(BaseImBlockDO::getSourceType, BaseImTypeEnum.GROUP).remove();

        });

        return TempBizCodeEnum.OK;

    }

}
