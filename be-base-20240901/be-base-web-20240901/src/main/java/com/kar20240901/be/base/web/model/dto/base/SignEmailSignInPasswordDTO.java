package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignEmailSignInPasswordDTO extends EmailNotBlankDTO {

    @NotBlank
    @Schema(description = "密码")
    private String password;

}
