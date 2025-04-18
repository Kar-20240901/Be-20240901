package com.kar20240901.be.base.web.model.domain.im;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_im_apply_group_extra")
@Data
@Schema(description = "子表：群组申请扩展表，主表：群组申请表")
public class BaseImApplyGroupExtraDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "群组申请主键 id")
    private Long applyGroupId;

    @Schema(description = "用户主键 id")
    private Long userId;

    @Schema(description = "是否隐藏")
    private Integer hiddenFlag;

}