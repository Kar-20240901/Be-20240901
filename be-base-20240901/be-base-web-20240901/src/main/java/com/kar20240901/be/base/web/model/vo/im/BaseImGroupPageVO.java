package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImGroupPageVO {

    @Schema(description = "群组主键 id")
    private String groupId;

    @Schema(description = "群组显示的 id")
    private String groupShowId;

    @Schema(description = "显示的名称")
    private String groupShowName;

    @Schema(description = "群组头像文件主键 id，后端用", hidden = true)
    private Long avatarFileId;

    @Schema(description = "群组头像地址")
    private String avatarUrl;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "是否是群主，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Boolean belongFlag;

    @Schema(description = "是否是管理员，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Boolean manageFlag;

    @Schema(description = "创建时间，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Date createTime;

    @Schema(description = "群组简介，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private String bio;

    @Schema(description = "是否被禁言，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Boolean muteFlag;

    @Schema(description = "普通成员是否禁言，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Boolean normalMuteFlag;

    @Schema(description = "管理员是否禁言，群主不会被禁言，备注：只有 dto的 manageQueryFlag生效时，才会返回该值")
    private Boolean manageMuteFlag;

}
