package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignEmailUpdateUserNameDTO {

    @Size(max = 20)
    @NotBlank
    @Pattern(regexp = TempRegexConstant.USER_NAME_REGEXP)
    @Schema(description = "用户名")
    private String username;

    @Pattern(regexp = TempRegexConstant.CODE_6_REGEXP)
    @NotBlank
    @Schema(description = "邮箱验证码")
    private String code;

}
