package com.kar20240901.be.base.web.model.dto.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserBankCardInsertOrUpdateDTO extends BaseUserBankCardInsertOrUpdateUserSelfDTO {

    @NotNull
    @Schema(description = "用户主键 id")
    private Long id;

}
