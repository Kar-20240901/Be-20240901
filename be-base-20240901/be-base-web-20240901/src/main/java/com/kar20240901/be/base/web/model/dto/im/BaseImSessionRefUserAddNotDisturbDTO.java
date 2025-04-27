package com.kar20240901.be.base.web.model.dto.im;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseImSessionRefUserAddNotDisturbDTO {

    @NotNull
    @Schema(description = "会话主键 id")
    private Long sessionId;

}
