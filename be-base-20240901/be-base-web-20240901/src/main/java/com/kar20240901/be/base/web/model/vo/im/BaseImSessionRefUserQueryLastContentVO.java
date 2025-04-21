package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSessionRefUserQueryLastContentVO {

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "最新消息，备注：会截断或者处理")
    private String lastContent;

    @Schema(description = "最新消息类型")
    private Integer lastContentType;

    @Schema(description = "最新消息创建时间")
    private String lastContentCreateTime;

    @Schema(description = "未读数量")
    private Integer unReadCount;

}
