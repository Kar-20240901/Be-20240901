package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_apply_group")
@Data
@Schema(description = "主表：群组申请表")
public class BaseImApplyGroupDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "申请目标群组的主键 id")
    private Long targetGroupId;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝")
    private BaseImApplyStatusEnum status;

    @Schema(description = "是否显示在申请列表")
    private Boolean showFlag;

    @Schema(description = "目标是否显示在申请列表")
    private Boolean targetShowFlag;

    @Schema(description = "拒绝理由")
    private String rejectReason;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "申请内容")
    private String applyContent;

}