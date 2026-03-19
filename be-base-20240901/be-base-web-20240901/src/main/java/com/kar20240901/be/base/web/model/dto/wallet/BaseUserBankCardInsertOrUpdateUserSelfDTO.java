package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import com.kar20240901.be.base.web.model.enums.wallet.BaseOpenBankNameEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BaseUserBankCardInsertOrUpdateUserSelfDTO {

    @NotBlank
    @Pattern(regexp = TempRegexConstant.BANK_DEBIT_CARD)
    @Schema(description = "卡号")
    private String bankCardNo;

    @NotNull
    @Schema(description = "开户行")
    private BaseOpenBankNameEnum openBankName;

    @NotBlank
    @Schema(description = "支行")
    private String branchBankName;

    @NotBlank
    @Schema(description = "收款人姓名")
    private String payeeName;

}
