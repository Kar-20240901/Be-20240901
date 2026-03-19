package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.enums.im.BaseImApplyStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_apply_friend")
@Data
@Schema(description = "主表：好友申请表")
public class BaseImApplyFriendDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "申请目标用户的主键 id")
    private Long targetUserId;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝")
    private BaseImApplyStatusEnum status;

    @Schema(description = "拒绝理由")
    private String rejectReason;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "申请内容")
    private String applyContent;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "会话主键 id，未通过时为：-1，通过了则赋值，并且后续不变")
    private Long sessionId;

}