package com.kar20240901.be.base.web.model.dto.base;

import com.cmcorg20230301.be.engine.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignEmailBindAccountDTO {

    @Size(max = 200)
    @NotBlank
    @Pattern(regexp = BaseRegexConstant.EMAIL)
    @Schema(description = "邮箱")
    private String email;

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String code;

    @NotBlank
    @Schema(description = "当前密码")
    private String currentPassword;

}
