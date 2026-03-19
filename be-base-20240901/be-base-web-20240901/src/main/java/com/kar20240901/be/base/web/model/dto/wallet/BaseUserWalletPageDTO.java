package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserWalletPageDTO extends MyPageDTO {

    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "提现金额：开始值")
    private BigDecimal beginWithdrawableMoney;

    @Schema(description = "提现金额：结束值")
    private BigDecimal endWithdrawableMoney;

    @Schema(description = "起始时间：更新时间")
    private Date utBeginTime;

    @Schema(description = "结束时间：更新时间")
    private Date utEndTime;

}
