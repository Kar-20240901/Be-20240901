package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignEmailSignInCodeDTO extends EmailNotBlankDTO {

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "手机验证码")
    private String code;

}
