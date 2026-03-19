package com.kar20240901.be.base.web.model.vo.im;

import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseImApplyGroupPageSelfVO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝 401 已取消")
    private BaseImApplyStatusEnum status;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "目标群组名")
    private String groupName;

    @Schema(description = "目标群组头像文件 id，后端用", hidden = true)
    private Long avatarFileId;

    @Schema(description = "目标群组头像")
    private String avatarUrl;

    @Schema(description = "群组主键 id")
    private Long groupId;

}
