package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignEmailSetPasswordDTO {

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String code;

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String password;

    @NotBlank
    @Schema(description = "前端加密之后的原始密码")
    private String originPassword;

}
