package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserService;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomUserServiceImpl extends ServiceImpl<BaseLiveRoomUserMapper, BaseLiveRoomUserDO>
    implements BaseLiveRoomUserService {

    /**
     * 新增用户
     */
    @Override
    public void addUser(Long roomId, Long userId, Long socketRefUserId) {

        Assert.notNull(roomId);
        Assert.notNull(userId);
        Assert.notNull(socketRefUserId);

        BaseLiveRoomUserDO baseLiveRoomUserDO = new BaseLiveRoomUserDO();

        baseLiveRoomUserDO.setRoomId(roomId);
        baseLiveRoomUserDO.setUserId(userId);
        baseLiveRoomUserDO.setSocketRefUserId(socketRefUserId);

        save(baseLiveRoomUserDO);

    }

}
