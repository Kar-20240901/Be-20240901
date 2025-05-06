package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomSelfService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomSelfServiceImpl extends ServiceImpl<BaseLiveRoomMapper, BaseLiveRoomDO>
    implements BaseLiveRoomSelfService {

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseLiveRoomSelfInsertOrUpdateDTO dto) {

        Long id = dto.getId();

        Long currentUserId = MyUserUtil.getCurrentUserId();

        if (id == null) {

            BaseLiveRoomDO baseLiveRoomDO = new BaseLiveRoomDO();

            baseLiveRoomDO.setBelongId(currentUserId);
            baseLiveRoomDO.setName(dto.getName());

            save(baseLiveRoomDO);

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

        return TempBizCodeEnum.OK;

    }

}
