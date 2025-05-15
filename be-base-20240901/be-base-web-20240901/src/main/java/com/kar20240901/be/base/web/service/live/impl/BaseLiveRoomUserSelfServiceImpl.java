package com.kar20240901.be.base.web.service.live.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomUserMapper;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserSelfService;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomUserSelfServiceImpl extends ServiceImpl<BaseLiveRoomUserMapper, BaseLiveRoomUserDO>
    implements BaseLiveRoomUserSelfService {

}
