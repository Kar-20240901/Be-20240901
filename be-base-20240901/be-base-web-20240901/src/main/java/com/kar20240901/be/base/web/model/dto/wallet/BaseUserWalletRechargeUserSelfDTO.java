package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseUserWalletRechargeUserSelfDTO {

    /**
     * {@link IBasePayType}
     */
    @Schema(description = "支付方式，备注：如果为 null，则表示用默认支付方式")
    private Integer payType;

    @NotNull
    @Schema(description = "值")
    private BigDecimal value;

}
