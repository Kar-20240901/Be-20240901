package com.kar20240901.be.base.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUserNameSignDeleteDTO {

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String currentPassword;

}
