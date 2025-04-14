package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_apply")
@Data
@Schema(description = "主表：申请表")
public class BaseImApplyDO extends TempEntity {

    @Schema(description = "会话主键 id，是私聊时：申请时为 -1，申请通过之后为：实际的会话主键 id")
    private Long sessionId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "会话是私聊时，申请目标用户的主键 id，其他类型时，该值为：-1")
    private Long privateChatApplyTargetUserId;

    @Schema(description = "申请加入会话的备注")
    private String remark;

    @Schema(description = "冗余字段，会话类型：101 私聊 201 群聊 301 客服")
    private Integer sessionType;

    @Schema(description = "状态：101 申请中 201 已通过 301 已拒绝 401 已被拉黑")
    private Integer status;

    @Schema(description = "拉黑前的状态，用于：取消拉黑之后恢复原始状态，初始值为：申请中")
    private Integer blockPreStatus;

    @Schema(description = "是否显示在申请列表")
    private Integer showFlag;

    @Schema(description = "拒绝理由")
    private String rejectReason;

}