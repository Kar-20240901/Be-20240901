package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_dept")
@Data
@Schema(description = "主表：部门表")
public class BaseDeptDO extends TempEntityTree<BaseDeptDO> {

    @Schema(description = "部门名")
    private String name;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "该部门的 uuid，备注：不能重复")
    private String uuid;

}