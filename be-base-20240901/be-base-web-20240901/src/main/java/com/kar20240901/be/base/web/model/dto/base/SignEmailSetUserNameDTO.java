package com.kar20240901.be.base.web.model.dto.base;

import com.cmcorg20230301.be.engine.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignEmailSetUserNameDTO {

    @Size(max = 20)
    @NotBlank
    @Pattern(regexp = BaseRegexConstant.SIGN_IN_NAME_REGEXP)
    @Schema(description = "用户名")
    private String signInName;

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String code;

}
