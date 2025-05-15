package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserPageVO;
import com.kar20240901.be.base.web.service.file.BaseFileService;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserSelfService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomUserSelfServiceImpl extends ServiceImpl<BaseLiveRoomUserMapper, BaseLiveRoomUserDO>
    implements BaseLiveRoomUserSelfService {

    @Resource
    BaseLiveRoomMapper baseLiveRoomMapper;

    @Resource
    BaseFileService baseFileService;

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseLiveRoomUserPageVO> myPage(BaseLiveRoomUserPageDTO dto) {

        Long roomId = dto.getRoomId();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists =
            lambdaQuery().eq(BaseLiveRoomUserDO::getUserId, currentUserId).eq(BaseLiveRoomUserDO::getRoomId, roomId)
                .exists();

        if (!exists) {
            R.error("操作失败：您不在房间里，无法获取房间人员信息", roomId);
        }

        Page<BaseLiveRoomUserPageVO> resPage = baseMapper.myPage(dto.createTimeDescDefaultOrderPage(), dto);

        Set<Long> avatarFileIdSet = resPage.getRecords().stream().map(BaseLiveRoomUserPageVO::getAvatarFileId)
            .filter(it -> it != TempConstant.NEGATIVE_ONE).collect(Collectors.toSet());

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(new NotEmptyIdSet(avatarFileIdSet)).getMap();

        for (BaseLiveRoomUserPageVO item : resPage.getRecords()) {

            String avatarUrl = publicUrlMap.get(item.getAvatarFileId());

            item.setAvatarUrl(avatarUrl);

            item.setAvatarFileId(null);

        }

        return resPage;

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseLiveRoomUserInfoByIdVO infoById(NotNullId dto) {

        BaseLiveRoomUserInfoByIdVO baseLiveRoomUserInfoByIdVO = baseMapper.infoById(dto.getId());

        if (baseLiveRoomUserInfoByIdVO == null) {
            return null;
        }

        Map<Long, String> publicUrlMap = baseFileService.getPublicUrl(
            new NotEmptyIdSet(CollUtil.newHashSet(baseLiveRoomUserInfoByIdVO.getAvatarFileId()))).getMap();

        baseLiveRoomUserInfoByIdVO.setAvatarUrl(publicUrlMap.get(baseLiveRoomUserInfoByIdVO.getAvatarFileId()));

        baseLiveRoomUserInfoByIdVO.setAvatarFileId(null);

        return baseMapper.infoById(dto.getId());

    }

    /**
     * 批量删除
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet dto) {

        Set<Long> idSet = dto.getIdSet();

        if (CollUtil.isEmpty(idSet)) {
            return TempBizCodeEnum.OK;
        }

        Long currentUserId = MyUserUtil.getCurrentUserId();

        List<BaseLiveRoomUserDO> baseLiveRoomUserDoList =
            lambdaQuery().in(BaseLiveRoomUserDO::getId, idSet).ne(BaseLiveRoomUserDO::getUserId, currentUserId)
                .select(BaseLiveRoomUserDO::getRoomId).list();

        if (CollUtil.isEmpty(baseLiveRoomUserDoList)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        Set<Long> roomIdSet =
            baseLiveRoomUserDoList.stream().map(BaseLiveRoomUserDO::getRoomId).collect(Collectors.toSet());

        Long count = ChainWrappers.lambdaQueryChain(baseLiveRoomMapper).in(BaseLiveRoomDO::getId, roomIdSet)
            .eq(BaseLiveRoomDO::getBelongId, currentUserId).count();

        if (count != roomIdSet.size()) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST, roomIdSet);
        }

        lambdaUpdate().in(BaseLiveRoomUserDO::getId, idSet).remove();

        return TempBizCodeEnum.OK;

    }

}
