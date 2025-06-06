package com.kar20240901.be.base.web.service.live;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDataDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomDataAddDataDTO;

public interface BaseLiveRoomDataService extends IService<BaseLiveRoomDataDO> {

    String addData(BaseLiveRoomDataAddDataDTO dto, ChannelDataBO channelDataBO);

}
