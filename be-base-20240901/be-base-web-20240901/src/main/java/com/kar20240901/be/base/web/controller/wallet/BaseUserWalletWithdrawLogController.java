package com.kar20240901.be.base.web.controller.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletWithdrawLogDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.NotNullIdAndStringValue;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletWithdrawLogPageUserSelfDTO;
import com.kar20240901.be.base.web.model.vo.base.DictIntegerVO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletWithdrawLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "基础-用户钱包-提现记录")
@RestController
@RequestMapping("/base/userWalletWithdrawLog")
public class BaseUserWalletWithdrawLogController {

    @Resource
    BaseUserWalletWithdrawLogService baseService;

    @Operation(summary = "下拉列表-提现状态")
    @PostMapping("/dictList/withdrawStatus")
    public R<Page<DictIntegerVO>> withdrawStatusDictList() {
        return R.okData(baseService.withdrawStatusDictList());
    }

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseUserWalletWithdrawLogInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "取消")
    @PostMapping("/cancel")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:cancel')")
    public R<String> cancel(@RequestBody @Valid NotNullId notNullId) {
        return R.okMsg(baseService.cancel(notNullId));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:page')")
    public R<Page<BaseUserWalletWithdrawLogDO>> myPage(@RequestBody @Valid BaseUserWalletWithdrawLogPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:infoById')")
    public R<BaseUserWalletWithdrawLogDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "分页排序查询-用户")
    @PostMapping("/page/userSelf")
    public R<Page<BaseUserWalletWithdrawLogDO>> myPageUserSelf(
        @RequestBody @Valid BaseUserWalletWithdrawLogPageUserSelfDTO dto) {
        return R.okData(baseService.myPageUserSelf(dto));
    }

    @Operation(summary = "新增/修改-用户")
    @PostMapping("/insertOrUpdate/userSelf")
    public R<String> insertOrUpdateUserSelf(
        @RequestBody @Valid BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO dto) {
        return R.okMsg(baseService.insertOrUpdateUserSelf(dto));
    }

    @Operation(summary = "取消-用户")
    @PostMapping("/cancel/userSelf")
    public R<String> cancelUserSelf(@RequestBody @Valid NotNullId notNullId) {
        return R.okMsg(baseService.cancelUserSelf(notNullId));
    }

    @Operation(summary = "受理-用户的提现记录")
    @PostMapping("/accept")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:accept')")
    public R<String> accept(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.accept(notEmptyIdSet));
    }

    @Operation(summary = "成功-用户的提现记录")
    @PostMapping("/success")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:success')")
    public R<String> success(@RequestBody @Valid NotNullId notNullId) {
        return R.okMsg(baseService.success(notNullId));
    }

    @Operation(summary = "拒绝-用户的提现记录")
    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('baseUserWalletWithdrawLog:reject')")
    public R<String> reject(@RequestBody @Valid NotNullIdAndStringValue notNullIdAndStringValue) {
        return R.okMsg(baseService.reject(notNullIdAndStringValue));
    }

}
