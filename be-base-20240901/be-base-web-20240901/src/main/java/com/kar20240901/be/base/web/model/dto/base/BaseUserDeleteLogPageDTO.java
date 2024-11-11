package com.kar20240901.be.base.web.model.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserDeleteLogPageDTO extends MyPageDTO {

    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "邮箱，可以为空")
    private String email;

    @Schema(description = "用户名，可以为空")
    private String username;

    @Schema(description = "手机号，可以为空")
    private String phone;

    @Schema(description = "微信 openId，可以为空")
    private String wxOpenId;

    @Schema(description = "微信 appId，可以为空")
    private String wxAppId;

    @Schema(description = "该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不允许修改")
    private String uuid;

    @Schema(description = "昵称")
    private String nickname;

}
