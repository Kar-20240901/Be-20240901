package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseImSessionRefUserPageVO extends BaseImSessionRefUserQueryLastContentVO {

    @Schema(description = "会话主键 id，一定有值")
    private Long sessionId;

    @Schema(description = "显示的会话名，一定有值")
    private String sessionName;

    @Schema(description = "冗余字段：头像 url，一定有值")
    private String avatarUrl;

    @Schema(description = "目标 id，可以查询到好友用户主键 id，或者群组主键 id，一定有值")
    private Long targetId;

    @Schema(description = "目标类型：101 好友 201 群组，一定有值")
    private Integer targetType;

}
