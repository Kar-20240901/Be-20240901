package com.kar20240901.be.base.web.controller.im;

import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSendDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImApplyFriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imApplyFriend")
@Tag(name = "基础-im-好友-申请")
public class BaseImApplyFriendController {

    @Resource
    BaseImApplyFriendService baseService;

    @Operation(summary = "发送好友申请")
    @PostMapping("/send")
    public R<String> send(@RequestBody @Valid BaseImApplyFriendSendDTO dto) {
        return R.okMsg(baseService.send(dto));
    }

}
