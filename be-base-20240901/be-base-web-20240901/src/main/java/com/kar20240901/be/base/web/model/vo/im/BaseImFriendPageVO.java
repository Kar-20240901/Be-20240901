package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImFriendPageVO {

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "好友主键 id")
    private Long friendId;

    @Schema(description = "好友显示名称")
    private String friendName;

    @Schema(description = "好友头像")
    private String avatarUrl;

}
