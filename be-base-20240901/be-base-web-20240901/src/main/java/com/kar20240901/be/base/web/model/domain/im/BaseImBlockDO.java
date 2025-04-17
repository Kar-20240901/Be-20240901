package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_block")
@Data
@Schema(description = "主表：拉黑表")
public class BaseImBlockDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人id")
    private Long createId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "会话主键 id，默认为 -1")
    private Long sessionId;

    @Schema(description = "来源 id")
    private Long sourceId;

    @Schema(description = "来源类型：101 好友 201 群组")
    private Integer sourceType;

}