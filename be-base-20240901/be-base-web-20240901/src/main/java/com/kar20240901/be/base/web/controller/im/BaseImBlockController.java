package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupAddUserDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImBlockGroupPageVO;
import com.kar20240901.be.base.web.service.im.BaseImBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imBlock")
@Tag(name = "基础-im-拉黑-管理")
public class BaseImBlockController {

    @Resource
    BaseImBlockService baseService;

    @Operation(summary = "拉黑好友")
    @PostMapping("/addFriend")
    public R<String> addFriend(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.addFriend(dto));
    }

    @Operation(summary = "群组拉黑用户")
    @PostMapping("/groupAddUser")
    public R<String> groupAddUser(@RequestBody @Valid BaseImBlockGroupAddUserDTO dto) {
        return R.okMsg(baseService.groupAddUser(dto));
    }

    @Operation(summary = "取消拉黑好友")
    @PostMapping("/cancelFriend")
    public R<String> cancelFriend(@RequestBody @Valid NotNullId dto) {
        return R.okMsg(baseService.cancelFriend(dto));
    }

    @Operation(summary = "取消群组拉黑用户")
    @PostMapping("/groupCancelUser")
    public R<String> groupCancelUser(@RequestBody @Valid BaseImBlockGroupAddUserDTO dto) {
        return R.okMsg(baseService.groupCancelUser(dto));
    }

    @Operation(summary = "群组分页排序查询拉黑用户")
    @PostMapping("/groupPage")
    public R<Page<BaseImBlockGroupPageVO>> groupPage(@RequestBody @Valid BaseImBlockGroupPageDTO dto) {
        return R.okData(baseService.groupPage(dto));
    }

}