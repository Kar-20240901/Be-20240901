package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserAddUserDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomUserServiceImpl extends ServiceImpl<BaseLiveRoomUserMapper, BaseLiveRoomUserDO>
    implements BaseLiveRoomUserService {

    @Resource
    BaseLiveRoomMapper baseLiveRoomMapper;

    /**
     * 新增用户
     */
    @Override
    @MyTransactional
    public String addUser(BaseLiveRoomUserAddUserDTO dto, ChannelDataBO channelDataBO) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BaseLiveRoomDO baseLiveRoomDO =
            ChainWrappers.lambdaQueryChain(baseLiveRoomMapper).eq(BaseLiveRoomDO::getId, dto.getId())
                .select(BaseLiveRoomDO::getCode, BaseLiveRoomDO::getBelongId).one();

        if (baseLiveRoomDO == null) {
            R.error("操作失败：该房间不存在", dto.getId());
        }

        if (!currentUserId.equals(baseLiveRoomDO.getBelongId())) {

            if (StrUtil.isNotBlank(baseLiveRoomDO.getCode())) {

                // 如果验证码不匹配
                if (baseLiveRoomDO.getCode().equalsIgnoreCase(dto.getCode()) == false) {
                    R.error("操作失败：房间验证码错误", dto.getCode());
                }

            }

        }

        BaseLiveRoomUserDO baseLiveRoomUserDO = new BaseLiveRoomUserDO();

        baseLiveRoomUserDO.setRoomId(dto.getId());
        baseLiveRoomUserDO.setUserId(channelDataBO.getUserId());
        baseLiveRoomUserDO.setSocketRefUserId(channelDataBO.getSocketRefUserId());

        boolean remove = lambdaUpdate().eq(BaseLiveRoomUserDO::getUserId, currentUserId)
            .eq(BaseLiveRoomUserDO::getRoomId, dto.getId()).remove();

        if (remove) {
            // 提示：您已经在其他设备上加入此房间
        }

        save(baseLiveRoomUserDO);

        return TempBizCodeEnum.OK;

    }

}
