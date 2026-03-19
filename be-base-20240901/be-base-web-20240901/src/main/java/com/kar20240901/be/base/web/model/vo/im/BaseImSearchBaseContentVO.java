package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSearchBaseContentVO {

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "来源类型")
    private Integer targetType;

    @Schema(description = "来源 id")
    private Long targetId;

    @Schema(description = "显示名称")
    private String showName;

    @Schema(description = "搜索的消息内容总数")
    private Long searchCount;

}
