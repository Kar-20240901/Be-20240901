package com.kar20240901.be.base.web.model.dto.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserWalletLogPageDTO extends BaseUserWalletLogUserSelfPageDTO {

    @Schema(description = "用户主键 id")
    private Long userId;

}
