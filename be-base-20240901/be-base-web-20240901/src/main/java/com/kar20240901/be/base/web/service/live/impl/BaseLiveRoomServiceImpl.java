package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomPageDTO;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomService;
import com.kar20240901.be.base.web.util.base.CodeUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomServiceImpl extends ServiceImpl<BaseLiveRoomMapper, BaseLiveRoomDO>
    implements BaseLiveRoomService {

    @Resource
    BaseLiveRoomUserMapper baseLiveRoomUserMapper;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseLiveRoomInsertOrUpdateDTO dto) {

        BaseLiveRoomDO baseLiveRoomDO = new BaseLiveRoomDO();

        baseLiveRoomDO.setId(dto.getId());
        baseLiveRoomDO.setBelongId(dto.getBelongId());
        baseLiveRoomDO.setName(dto.getName());

        if (dto.getId() == null) {

            if (BooleanUtil.isFalse(dto.getCodeFlag())) {

                baseLiveRoomDO.setCode("");

            } else {

                baseLiveRoomDO.setCode(CodeUtil.getCode());

            }

        }

        saveOrUpdate(baseLiveRoomDO);

        return TempBizCodeEnum.OK;

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseLiveRoomDO> myPage(BaseLiveRoomPageDTO dto) {

        return baseMapper.myPage(dto.createTimeDescDefaultOrderPage(), dto);

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
    @MyTransactional
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

        lambdaUpdate().eq(BaseLiveRoomDO::getId, dto.getId()).set(BaseLiveRoomDO::getCode, CodeUtil.getCode()).update();

        return TempBizCodeEnum.OK;

    }

}
