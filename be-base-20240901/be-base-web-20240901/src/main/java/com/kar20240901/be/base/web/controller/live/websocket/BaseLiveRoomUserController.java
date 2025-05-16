package com.kar20240901.be.base.web.controller.live.websocket;

import com.kar20240901.be.base.web.model.annotation.base.NettyWebSocketController;
import com.kar20240901.be.base.web.model.bo.socket.ChannelDataBO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserAddUserDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@NettyWebSocketController
@RequestMapping(value = "/base/liveRoomUser")
@Tag(name = "websocket-基础-实时房间-用户")
public class BaseLiveRoomUserController {

    @Resource
    BaseLiveRoomUserService baseService;

    @Operation(summary = "用户加入房间")
    @PostMapping("/addUser")
    public R<Long> addUser(@Valid BaseLiveRoomUserAddUserDTO dto, ChannelDataBO channelDataBO) {
        return R.okData(baseService.addUser(dto, channelDataBO));
    }

}
