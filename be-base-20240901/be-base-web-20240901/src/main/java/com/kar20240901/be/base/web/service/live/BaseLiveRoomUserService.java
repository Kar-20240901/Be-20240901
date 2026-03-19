package com.kar20240901.be.base.web.service.live;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserAddUserDTO;

public interface BaseLiveRoomUserService extends IService<BaseLiveRoomUserDO> {

    Long addUser(BaseLiveRoomUserAddUserDTO dto, ChannelDataBO channelDataBO);

}
