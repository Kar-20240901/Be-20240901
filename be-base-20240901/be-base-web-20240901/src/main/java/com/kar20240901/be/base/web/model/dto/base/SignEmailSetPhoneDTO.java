package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignEmailSetPhoneDTO {

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String emailCode;

    @NotBlank
    @Pattern(regexp = TempRegexConstant.PHONE)
    @Schema(description = "手机号码")
    private String phone;

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "手机验证码")
    private String phoneCode;

}
