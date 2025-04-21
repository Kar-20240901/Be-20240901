package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImApplyFriendSearchApplyGroupVO {

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "群组名称")
    private String name;

}
