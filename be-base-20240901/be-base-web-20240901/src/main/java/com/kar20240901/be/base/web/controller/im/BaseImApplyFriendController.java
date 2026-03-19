package com.kar20240901.be.base.web.controller.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyFriendDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSendDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyFriendVO;
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

    @Operation(summary = "搜索要添加的好友")
    @PostMapping("/searchApplyFriend")
    public R<Page<BaseImApplyFriendSearchApplyFriendVO>> searchApplyFriend(
        @RequestBody @Valid BaseImApplyFriendSearchApplyFriendDTO dto) {
        return R.okData(baseService.searchApplyFriend(dto));
    }

    @Operation(summary = "发送好友申请")
    @PostMapping("/send")
    public R<String> send(@RequestBody @Valid BaseImApplyFriendSendDTO dto) {
        return R.okMsg(baseService.send(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    public R<Page<BaseImApplyFriendPageVO>> myPage(@RequestBody @Valid BaseImApplyFriendPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "同意")
    @PostMapping("/agree")
    public R<String> agree(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.agree(dto));
    }

    @Operation(summary = "拒绝")
    @PostMapping("/reject")
    public R<String> reject(@RequestBody @Valid BaseImApplyFriendRejectDTO dto) {
        return R.okMsg(baseService.reject(dto));
    }

    @Operation(summary = "隐藏")
    @PostMapping("/hidden")
    public R<String> hidden(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.hidden(dto));
    }

    @Operation(summary = "取消")
    @PostMapping("/cancel")
    public R<String> cancel(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okMsg(baseService.cancel(dto));
    }

}
