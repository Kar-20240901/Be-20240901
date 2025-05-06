package com.kar20240901.be.base.web.service.live;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;

public interface BaseLiveRoomUserService extends IService<BaseLiveRoomUserDO> {

    void addUser(Long roomId, Long userId, Long socketRefUserId);

}
