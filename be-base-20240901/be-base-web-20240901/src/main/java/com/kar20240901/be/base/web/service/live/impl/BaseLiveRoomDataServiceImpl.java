package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDataDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomDataAddDataDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomDataService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomDataServiceImpl extends ServiceImpl<BaseLiveRoomDataMapper, BaseLiveRoomDataDO>
    implements BaseLiveRoomDataService {

    @Resource
    BaseLiveRoomUserMapper baseLiveRoomUserMapper;

    /**
     * 新增数据
     */
    @Override
    public String addData(BaseLiveRoomDataAddDataDTO dto) {

        long currentTimeMillis = System.currentTimeMillis();

        if (dto.getCreateTs() > currentTimeMillis) {

            R.error("操作失败：数据不合法",
                StrUtil.format("当前时间：{}，传递时间：{}", currentTimeMillis, dto.getCreateTs()));

        }

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists =
            ChainWrappers.lambdaQueryChain(baseLiveRoomUserMapper).eq(BaseLiveRoomUserDO::getUserId, currentUserId)
                .eq(BaseLiveRoomUserDO::getRoomId, dto.getRoomId()).exists();

        if (!exists) {
            R.error("操作失败：您不在房间内", dto.getRoomId());
        }

        BaseLiveRoomDataDO baseLiveRoomDataDO = new BaseLiveRoomDataDO();

        baseLiveRoomDataDO.setRoomId(dto.getRoomId());
        baseLiveRoomDataDO.setCreateTs(dto.getCreateTs());
        baseLiveRoomDataDO.setData(dto.getData());
        baseLiveRoomDataDO.setCreateId(currentUserId);

        save(baseLiveRoomDataDO);

        return TempBizCodeEnum.OK;

    }

}
