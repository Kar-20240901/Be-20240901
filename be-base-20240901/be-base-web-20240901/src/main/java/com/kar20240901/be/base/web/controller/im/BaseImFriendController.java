package com.kar20240901.be.base.web.controller.im;

import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImFriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imFriend")
@Tag(name = "基础-im-好友-管理")
public class BaseImFriendController {

    @Resource
    BaseImFriendService baseService;

    @Operation(summary = "删除好友")
    @PostMapping("/removeFriend")
    public R<String> removeFriend(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.removeFriend(dto));
    }

}