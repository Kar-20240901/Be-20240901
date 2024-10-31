package com.kar20240901.be.base.web.model.dto.base;

import com.cmcorg20230301.be.engine.model.model.constant.BaseRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignEmailSetPhoneDTO {

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String emailCode;

    @Size(max = 100)
    @NotBlank
    @Pattern(regexp = BaseRegexConstant.PHONE)
    @Schema(description = "手机号码")
    private String phone;

    @Pattern(regexp = BaseRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "手机验证码")
    private String phoneCode;

}
