package com.kar20240901.be.base.web.model.vo.im;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImGroupRefUserPageVO {

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像文件主键 id，后端用", hidden = true)
    private Long avatarFileId;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "用户编码")
    private String uuid;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "是否禁言")
    private Boolean muteFlag;

    @Schema(
        description = "是否管理员：可以：审批入群，修改群名称，群头像，踢出群员，禁言群员，不能：赋权另外一个管理员，群组创建人不用该字段，并且群组创建人该字段为 false")
    private Boolean manageFlag;

    @Schema(description = "是否群组创建人")
    private Boolean belongFlag;

    @Schema(description = "是否拉黑")
    private Boolean blockFlag;

    @Schema(description = "入群时间")
    private Date createTime;

}
