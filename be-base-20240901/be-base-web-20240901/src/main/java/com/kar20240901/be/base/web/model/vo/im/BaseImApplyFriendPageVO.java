package com.kar20240901.be.base.web.model.vo.im;

import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImApplyFriendPageVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝 401 已取消")
    private BaseImApplyStatusEnum status;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "目标用户昵称")
    private String nickname;

    @Schema(description = "目标用户头像文件 id，后端用", hidden = true)
    private Long avatarFileId;

    @Schema(description = "目标用户头像")
    private String avatarUrl;

    @Schema(description = "拒绝理由")
    private String rejectReason;

    @Schema(description = "修改时间，包含：申请时间、取消时间、通过时间、拒绝时间")
    private Date updateTime;

    @Schema(description = "目标用户主键 id，用于：拉黑功能")
    private Long userId;

    @Schema(description = "是否已经拉黑，备注：只有 dto的 toMeFlag生效时，才会返回该值")
    private Boolean blockFlag;

}
