package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_im_session_content")
@Data
@Schema(description = "子表：会话内容表，主表：会话表")
public class BaseImSessionContentDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人id")
    private Long createId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "是否启用，用于撤回功能")
    private Integer enableFlag;

    @Schema(description = "会话主键 id")
    private Long sessionId;

    @Schema(description = "会话内容")
    private String content;

    @Schema(description = "内容类型")
    private Integer type;

    @Schema(description = "创建时间的时间戳，UTC+8")
    private Long createTs;

    @Schema(description = "引用的内容主键 id，不引用时为 -1")
    private Long refId;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

}