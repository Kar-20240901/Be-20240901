package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseImApplyFriendSearchApplyFriendVO {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "用户昵称")
    private String nickname;

}
