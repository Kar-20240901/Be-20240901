package com.kar20240901.be.base.web.controller.live;

import com.kar20240901.be.base.web.service.live.BaseLiveRoomUserSelfService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/liveRoomUserSelf")
@Tag(name = "基础-实时-房间-用户-管理-自我")
public class BaseLiveRoomUserSelfController {

    @Resource
    BaseLiveRoomUserSelfService baseService;

}
