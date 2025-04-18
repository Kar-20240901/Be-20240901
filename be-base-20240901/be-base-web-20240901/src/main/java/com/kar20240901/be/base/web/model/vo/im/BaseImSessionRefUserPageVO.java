package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImSessionRefUserPageVO {

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "显示的会话名")
    private String name;

    @Schema(description = "冗余字段：头像 url")
    private Long avatarUrl;

    @Schema(description = "最新消息，备注：会截断或者处理")
    private String lastContent;

    @Schema(description = "最新消息，备注：会截断或者处理")
    private String lastContentType;

    @Schema(description = "未读数量")
    private Integer unReadCount;

}
