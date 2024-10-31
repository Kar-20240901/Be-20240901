package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseUserConfigurationInsertOrUpdateDTO {

    @Schema(description = "是否启用：用户名注册功能")
    private Boolean userNameSignUpEnable;

    @Schema(description = "是否启用：邮箱注册功能")
    private Boolean emailSignUpEnable;

    @Schema(description = "是否启用：手机号码注册功能")
    private Boolean phoneSignUpEnable;

}
