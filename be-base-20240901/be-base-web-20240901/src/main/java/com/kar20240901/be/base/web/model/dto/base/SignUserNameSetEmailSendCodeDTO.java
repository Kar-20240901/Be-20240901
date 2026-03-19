package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignUserNameSetEmailSendCodeDTO {

    @NotBlank
    @Pattern(regexp = TempRegexConstant.EMAIL)
    @Schema(description = "邮箱")
    private String email;

}
