package com.kar20240901.be.base.web.controller.live.websocket;

import com.kar20240901.be.base.web.model.annotation.base.NettyWebSocketController;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomDataAddDataDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@NettyWebSocketController
@RequestMapping(value = "/base/liveRoomData")
@Tag(name = "基础-实时房间-数据")
public class BaseLiveRoomDataController {

    @Resource
    BaseLiveRoomDataService baseService;

    @Operation(summary = "新增数据")
    @PostMapping("/addData")
    public R<String> addData(@Valid BaseLiveRoomDataAddDataDTO dto) {
        return R.okMsg(baseService.addData(dto));
    }

}
