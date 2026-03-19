package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.wallet.BaseUserWalletWithdrawStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserWalletWithdrawLogPageUserSelfDTO extends MyPageDTO {

    @Schema(description = "提现编号")
    private Long id;

    @Schema(description = "卡号")
    private String bankCardNo;

    @Schema(description = "开户行")
    private String openBankName;

    @Schema(description = "支行")
    private String branchBankName;

    @Schema(description = "收款人姓名")
    private String payeeName;

    @Schema(description = "提现状态")
    private BaseUserWalletWithdrawStatusEnum withdrawStatus;

    @Schema(description = "拒绝理由")
    private String rejectReason;

    @Schema(description = "起始时间：创建时间")
    private Date ctBeginTime;

    @Schema(description = "结束时间：创建时间")
    private Date ctEndTime;

    @Schema(description = "提现金额：开始值")
    private BigDecimal beginWithdrawMoney;

    @Schema(description = "提现金额：结束值")
    private BigDecimal endWithdrawMoney;

}
