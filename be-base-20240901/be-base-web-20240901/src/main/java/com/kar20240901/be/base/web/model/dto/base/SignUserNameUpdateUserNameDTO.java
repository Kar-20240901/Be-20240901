package com.kar20240901.be.base.web.model.dto.base;

import com.kar20240901.be.base.web.model.constant.base.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUserNameUpdateUserNameDTO {

    @Size(max = 100)
    @NotBlank
    @Pattern(regexp = TempRegexConstant.USER_NAME_REGEXP)
    @Schema(description = "新用户名")
    private String newUserName;

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String currentPassword;

}
