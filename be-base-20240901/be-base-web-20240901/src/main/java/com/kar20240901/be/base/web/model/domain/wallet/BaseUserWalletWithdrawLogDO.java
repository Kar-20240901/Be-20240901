package com.kar20240901.be.base.web.model.domain.wallet;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.enums.wallet.BaseOpenBankNameEnum;
import com.kar20240901.be.base.web.model.enums.wallet.BaseUserWalletWithdrawStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_user_wallet_withdraw_log")
@Data
@Schema(description = "主表：用户钱包提现记录表")
public class BaseUserWalletWithdrawLogDO extends TempEntity {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "提现金额")
    private BigDecimal withdrawMoney;

    @Schema(description = "冗余字段：卡号")
    private String bankCardNo;

    @Schema(description = "冗余字段：开户行")
    private BaseOpenBankNameEnum openBankName;

    @Schema(description = "冗余字段：支行")
    private String branchBankName;

    @Schema(description = "冗余字段：收款人姓名")
    private String payeeName;

    @Schema(description = "提现状态")
    private BaseUserWalletWithdrawStatusEnum withdrawStatus;

    @Schema(description = "拒绝理由")
    private String rejectReason;

}
