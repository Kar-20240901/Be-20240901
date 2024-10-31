package com.kar20240901.be.base.web.model.dto.base;

import com.cmcorg20230301.be.engine.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignEmailForgetPasswordDTO extends EmailNotBlankDTO {

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String code;

    @NotBlank
    @Schema(description = "前端加密之后的新密码")
    private String newPassword;

    @NotBlank
    @Schema(description = "前端加密之后的原始新密码")
    private String originNewPassword;

}
