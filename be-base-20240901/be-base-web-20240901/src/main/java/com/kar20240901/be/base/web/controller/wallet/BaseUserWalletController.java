package com.kar20240901.be.base.web.controller.wallet;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeBigDecimalNumberIdSetDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullLong;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletPageDTO;
import com.kar20240901.be.base.web.model.dto.wallet.BaseUserWalletRechargeUserSelfDTO;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.model.vo.pay.BuyVO;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "基础-用户钱包-管理")
@RestController
@RequestMapping("/base/userWallet")
public class BaseUserWalletController {

    @Resource
    BaseUserWalletService baseService;

    @Operation(summary = "批量冻结")
    @PostMapping("/frozenByIdSet")
    @PreAuthorize("hasAuthority('baseUserWallet:frozenByIdSet')")
    public R<String> frozenByIdSet(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okData(baseService.frozenByIdSet(dto));
    }

    @Operation(summary = "批量解冻")
    @PostMapping("/thawByIdSet")
    @PreAuthorize("hasAuthority('baseUserWallet:thawByIdSet')")
    public R<String> thawByIdSet(@RequestBody @Valid NotEmptyIdSet dto) {
        return R.okData(baseService.thawByIdSet(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseUserWallet:page')")
    public R<Page<BaseUserWalletDO>> myPage(@RequestBody @Valid BaseUserWalletPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseUserWallet:infoById')")
    public R<BaseUserWalletDO> infoById(@RequestBody @Valid NotNullLong dto) {
        return R.okData(baseService.infoById(dto));
    }

    @Operation(summary = "通过主键 idSet，加减可提现的钱")
    @PostMapping("/addWithdrawableMoney/background")
    @PreAuthorize("hasAuthority('baseUserWallet:addWithdrawableMoney')")
    public R<String> addWithdrawableMoneyBackground(@RequestBody @Valid ChangeBigDecimalNumberIdSetDTO dto) {
        return R.okMsg(baseService.addWithdrawableMoneyBackground(dto));
    }

    @Operation(summary = "通过主键id，查看详情-用户")
    @PostMapping("/infoById/userSelf")
    public R<BaseUserWalletDO> infoByIdUserSelf() {
        return R.okData(baseService.infoByIdUserSelf());
    }

    @Operation(summary = "充值-用户自我")
    @PostMapping("/recharge/userSelf")
    @PreAuthorize("hasAuthority('baseUserWallet:rechargeUserSelf')")
    public R<BuyVO> rechargeUserSelf(@RequestBody @Valid BaseUserWalletRechargeUserSelfDTO dto) {
        return R.okData(baseService.rechargeUserSelf(dto));
    }

}
