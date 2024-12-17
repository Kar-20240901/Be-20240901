package com.kar20240901.be.base.web.model.dto.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO {

    @NotNull
    @Min(1)
    @Schema(description = "提现金额")
    private BigDecimal withdrawMoney;

}
