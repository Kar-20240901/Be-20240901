package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_im_session")
@Data
@Schema(description = "主表：会话表")
public class BaseImSessionDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "会话主键 id")
    private Long id;

    @Schema(description = "来源 id，目的：删除好友/群组之后，还可以恢复之前的会话内容")
    private Long sourceId;

    @Schema(description = "来源类型：101 好友 201 群组")
    private Integer sourceType;

    @Schema(description = "冗余字段：头像 fileId（文件主键 id）")
    private Long avatarFileId;

}