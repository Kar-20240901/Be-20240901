package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupAgreeDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupHiddenGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageGroupDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupPageSelfDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageGroupVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyGroupPageSelfVO;
import com.kar20240901.be.base.web.service.im.BaseImApplyGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/imApplyGroup")
@Tag(name = "基础-im-群组-申请")
public class BaseImApplyGroupController {

    @Resource
    BaseImApplyGroupService baseService;

    @Operation(summary = "搜索要添加的群组")
    @PostMapping("/searchApplyGroup")
    public R<Page<BaseImApplyFriendSearchApplyGroupVO>> searchApplyGroup(
        @RequestBody @Valid BaseImApplyFriendSearchApplyGroupDTO dto) {
        return R.okData(baseService.searchApplyGroup(dto));
    }

    @Operation(summary = "发送入群申请")
    @PostMapping("/send")
    public R<String> send(@RequestBody @Valid BaseImApplyGroupSendDTO dto) {
        return R.okMsg(baseService.send(dto));
    }

    @Operation(summary = "分页排序查询-我的入群申请")
    @PostMapping("/pageSelf")
    public R<Page<BaseImApplyGroupPageSelfVO>> myPageSelf(@RequestBody @Valid BaseImApplyGroupPageSelfDTO dto) {
        return R.okData(baseService.myPageSelf(dto));
    }

    @Operation(summary = "分页排序查询-群组的入群申请")
    @PostMapping("/pageGroup")
    public R<Page<BaseImApplyGroupPageGroupVO>> myPageGroup(@RequestBody @Valid BaseImApplyGroupPageGroupDTO dto) {
        return R.okData(baseService.myPageGroup(dto));
    }

    @Operation(summary = "同意")
    @PostMapping("/agree")
    public R<String> agree(@RequestBody @Valid BaseImApplyGroupAgreeDTO dto) {
        return R.okMsg(baseService.agree(dto));
    }

    @Operation(summary = "拒绝")
    @PostMapping("/reject")
    public R<String> reject(@RequestBody @Valid BaseImApplyGroupRejectDTO dto) {
        return R.okMsg(baseService.reject(dto));
    }

    @Operation(summary = "隐藏-自我")
    @PostMapping("/hiddenSelf")
    public R<String> hiddenSelf(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.hiddenSelf(dto));
    }

    @Operation(summary = "隐藏-群组")
    @PostMapping("/hiddenGroup")
    public R<String> hiddenGroup(@RequestBody @Valid BaseImApplyGroupHiddenGroupDTO dto) {
        return R.okMsg(baseService.hiddenGroup(dto));
    }

}