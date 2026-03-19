package com.kar20240901.be.base.web.model.vo.im;

import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImApplyGroupPageGroupVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝")
    private BaseImApplyStatusEnum status;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "目标用户昵称")
    private String nickname;

    @Schema(description = "目标用户头像文件 id，后端用", hidden = true)
    private Long avatarFileId;

    @Schema(description = "目标用户头像")
    private String avatarUrl;

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "发起申请的用户主键 id")
    private Long applyUserId;

    @Schema(description = "群组名称")
    private String groupName;

    @Schema(description = "群组头像文件 id，后端用", hidden = true)
    private Long groupAvatarFileId;

    @Schema(description = "群组头像")
    private String groupAvatarUrl;

    @Schema(description = "是否已经拉黑")
    private Boolean blockFlag;

    @Schema(description = "用户编码")
    private String uuid;

    @Schema(description = "群聊编码")
    private String groupUuid;

}
