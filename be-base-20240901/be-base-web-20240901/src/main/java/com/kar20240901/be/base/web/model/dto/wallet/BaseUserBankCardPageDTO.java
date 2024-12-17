package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.dto.base.MyPageDTO;
import com.kar20240901.be.base.web.model.enums.wallet.BaseOpenBankNameEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserBankCardPageDTO extends MyPageDTO {

    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "卡号")
    private String bankCardNo;

    @Schema(description = "开户行")
    private BaseOpenBankNameEnum openBankName;

    @Schema(description = "支行")
    private String branchBankName;

    @Schema(description = "收款人姓名")
    private String payeeName;

}
