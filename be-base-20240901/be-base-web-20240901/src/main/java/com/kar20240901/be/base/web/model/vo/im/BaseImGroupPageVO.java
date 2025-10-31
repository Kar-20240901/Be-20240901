package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImGroupPageVO {

    @Schema(description = "群组主键 id")
    private String groupId;

    @Schema(description = "群组显示的 id")
    private String groupShowId;

    @Schema(description = "显示的名称")
    private String groupShowName;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "会话主键 id")
    private Long sessionId;

}
