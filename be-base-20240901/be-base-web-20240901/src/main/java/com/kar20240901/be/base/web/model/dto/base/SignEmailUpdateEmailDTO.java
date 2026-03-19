package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignEmailUpdateEmailDTO {

    @NotBlank
    @Pattern(regexp = TempRegexConstant.EMAIL)
    @Schema(description = "新邮箱")
    private String newEmail;

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "旧邮箱验证码")
    private String oldEmailCode;

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "新邮箱验证码")
    private String newEmailCode;

}
