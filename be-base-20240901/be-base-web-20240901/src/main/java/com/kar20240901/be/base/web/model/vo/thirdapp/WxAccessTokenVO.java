package com.kar20240901.be.base.web.model.vo.thirdapp;

import cn.hutool.core.annotation.Alias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxAccessTokenVO extends WxBaseVO {

    @Alias(value = "access_token")
    @Schema(description = "获取到的凭证")
    private String accessToken;

    @Alias(value = "expires_in")
    @Schema(description = "凭证有效时间，单位：秒。目前是7200秒之内的值。")
    private Long expiresIn;

}
