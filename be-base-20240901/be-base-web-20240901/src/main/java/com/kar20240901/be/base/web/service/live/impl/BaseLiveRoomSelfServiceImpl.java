package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomSelfService;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserService;
import com.kar20240901.be.base.web.util.base.CodeUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomSelfServiceImpl extends ServiceImpl<BaseLiveRoomMapper, BaseLiveRoomDO>
    implements BaseLiveRoomSelfService {

    @Resource
    BaseLiveRoomUserService baseLiveRoomUserService;

    /**
     * 新增/修改
     */
    @Override
    @MyTransactional
    public String insertOrUpdate(BaseLiveRoomSelfInsertOrUpdateDTO dto) {

        Long id = dto.getId();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (id == null) {

            BaseLiveRoomDO baseLiveRoomDO = new BaseLiveRoomDO();

            baseLiveRoomDO.setBelongId(currentUserId);
            baseLiveRoomDO.setName(dto.getName());
            baseLiveRoomDO.setCode(CodeUtil.getCode());

            save(baseLiveRoomDO);

            baseLiveRoomUserService.addUser(baseLiveRoomDO.getId(), currentUserId);

        } else {

            boolean exists =
                lambdaQuery().eq(BaseLiveRoomDO::getBelongId, currentUserId).eq(BaseLiveRoomDO::getId, id).exists();

            if (!exists) {
                R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
            }

            BaseLiveRoomDO baseLiveRoomDO = new BaseLiveRoomDO();

            baseLiveRoomDO.setId(dto.getId());
            baseLiveRoomDO.setName(dto.getName());

            updateById(baseLiveRoomDO);

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseLiveRoomDO> myPage(BaseLiveRoomSelfPageDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        return lambdaQuery().eq(BaseLiveRoomDO::getBelongId, currentUserId).page(dto.createTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseLiveRoomDO infoById(NotNullId dto) {

        return lambdaQuery().eq(BaseLiveRoomDO::getId, dto.getId()).one();

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

        lambdaUpdate().eq(BaseLiveRoomDO::getBelongId, currentUserId).in(BaseLiveRoomDO::getId, idSet).remove();

        ChainWrappers.lambdaUpdateChain(baseLiveRoomUserMapper).in(BaseLiveRoomUserDO::getRoomId, dto.getIdSet())
            .remove();

        return TempBizCodeEnum.OK;

    }

    /**
     * 刷新验证码
     */
    @Override
    public String refreshCode(NotNullId dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists =
            lambdaQuery().eq(BaseLiveRoomDO::getBelongId, currentUserId).eq(BaseLiveRoomDO::getId, dto.getId())
                .exists();

        if (!exists) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        lambdaUpdate().eq(BaseLiveRoomDO::getId, dto.getId()).set(BaseLiveRoomDO::getCode, CodeUtil.getCode()).update();

        return TempBizCodeEnum.OK;

    }

}
