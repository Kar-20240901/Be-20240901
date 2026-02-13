package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImFriendPageVO {

    @Schema(description = "好友用户主键 id")
    private Long friendUserId;

    @Schema(description = "好友显示的 id")
    private String friendShowId;

    @Schema(description = "好友显示名称")
    private String friendShowName;

    @Schema(description = "好友头像")
    private String avatarUrl;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "是否已经拉黑，备注：只有 dto的 queryBlockFlag生效时，才会返回该值")
    private Boolean blockFlag;

    @Schema(description = "个人简介，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private String bio;

    @Schema(description = "好友创建时间，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Date friendCreateTime;

    @Schema(description = "是否免打扰，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Boolean notDisturbFlag;

    @Schema(description = "im好友主键 id")
    private Long imFriendId;

}
