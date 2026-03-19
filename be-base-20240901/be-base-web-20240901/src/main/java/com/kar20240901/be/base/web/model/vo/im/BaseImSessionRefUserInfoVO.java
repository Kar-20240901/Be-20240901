package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSessionRefUserInfoVO {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "展示的名称")
    private String showName;

    @Schema(description = "头像 url")
    private String avatarUrl;

}
