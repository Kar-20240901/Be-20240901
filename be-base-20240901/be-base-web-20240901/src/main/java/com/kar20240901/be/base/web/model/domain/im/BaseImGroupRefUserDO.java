package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_im_group_ref_user")
@Data
@Schema(description = "关联表：群组表，用户表")
public class BaseImGroupRefUserDO extends TempEntityNoIdSuper {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "群组主键 id")
    private Long groupId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "我在群组的昵称")
    private String groupNickname;

}