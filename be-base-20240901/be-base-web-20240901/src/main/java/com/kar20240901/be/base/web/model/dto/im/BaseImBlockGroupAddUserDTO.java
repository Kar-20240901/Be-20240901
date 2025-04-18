package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseImBlockGroupAddUserDTO {

    @NotNull
    @Schema(description = "群组主键 id")
    private Long groupId;

    @NotNull
    @Schema(description = "用户主键 id")
    private Long userId;

}
