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

    @Schema(description = "管理员用户是否可以登录，备注：超级管理员除外")
    private Boolean manageSignInEnable;

    @Schema(description = "普通用户是否可以登录，备注：管理员和超级管理员除外")
    private Boolean normalSignInEnable;

}
