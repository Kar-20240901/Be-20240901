package com.kar20240901.be.base.web.model.dto.wallet;

import com.kar20240901.be.base.web.model.dto.base.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserWalletInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @Schema(description = "是否启用")
    private Boolean enableFlag;

}
