package com.kar20240901.be.base.web.model.vo.thirdapp;

import cn.hutool.core.annotation.Alias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxWorkOpenIdVO extends WxBaseVO {

    // 当用户为企业成员时 ↓

    @Schema(description = "成员UserID")
    private String userid;

    // 当用户为企业成员时 ↑

    // 非企业成员时 ↓

    @Alias(value = "external_userid")
    @Schema(description = "外部联系人id")
    private String externalUserid;

    // 非企业成员时 ↑

}
