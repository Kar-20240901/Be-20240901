package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserAddMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserDeleteMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserMutePageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupRefUserPageVO;
import com.kar20240901.be.base.web.service.im.BaseImGroupRefUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imGroupRefUser")
@Tag(name = "基础-im-群组-成员-管理")
public class BaseImGroupRefUserController {

    @Resource
    BaseImGroupRefUserService baseService;

    @Operation(summary = "群组分页排序查询群员")
    @PostMapping("/page")
    public R<Page<BaseImGroupRefUserPageVO>> myPage(@RequestBody @Valid BaseImGroupRefUserPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "群组分页排序查询-禁言用户")
    @PostMapping("/pageMute")
    public R<Page<BaseImGroupRefUserPageVO>> pageMute(@RequestBody @Valid BaseImGroupRefUserMutePageDTO dto) {
        return R.okData(baseService.pageMute(dto));
    }

    @Operation(summary = "新增禁言")
    @PostMapping("/addMute")
    public R<String> addMute(@RequestBody @Valid BaseImGroupRefUserAddMuteDTO dto) {
        return R.okMsg(baseService.addMute(dto));
    }

    @Operation(summary = "解除禁言")
    @PostMapping("/deleteMute")
    public R<String> deleteMute(@RequestBody @Valid BaseImGroupRefUserDeleteMuteDTO dto) {
        return R.okMsg(baseService.deleteMute(dto));
    }

    @Operation(summary = "新增管理员")
    @PostMapping("/addManage")
    public R<String> addManage(@RequestBody @Valid BaseImGroupRefUserAddMuteDTO dto) {
        return R.okMsg(baseService.addManage(dto));
    }

    @Operation(summary = "解除管理员")
    @PostMapping("/deleteManage")
    public R<String> deleteManage(@RequestBody @Valid BaseImGroupRefUserDeleteMuteDTO dto) {
        return R.okMsg(baseService.deleteManage(dto));
    }

    @Operation(summary = "群员退出-自我")
    @PostMapping("/leaveSelf")
    public R<String> leaveSelf(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.leaveSelf(dto));
    }

}