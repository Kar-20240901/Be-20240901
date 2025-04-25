package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_group")
@Data
@Schema(description = "主表：群组表")
public class BaseImGroupDO extends TempEntityNoIdSuper {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "归属者主键 id")
    private Long belongId;

    @Schema(description = "群组名称")
    private String name;

    @Schema(description = "头像 fileId（文件主键 id）")
    private Long avatarFileId;

    @Schema(description = "会话主键 id")
    private Long sessionId;

}