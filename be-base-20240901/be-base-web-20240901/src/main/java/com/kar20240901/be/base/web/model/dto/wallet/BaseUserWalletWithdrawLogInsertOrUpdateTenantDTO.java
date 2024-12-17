package com.kar20240901.be.base.web.model.dto.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserWalletWithdrawLogInsertOrUpdateTenantDTO
    extends BaseUserWalletWithdrawLogInsertOrUpdateUserSelfDTO {

    @NotNull
    @Schema(description = "租户主键 id")
    private Long tenantId;

}
