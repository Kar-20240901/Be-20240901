package com.kar20240901.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.TempEntityTree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_post")
@Data
@Schema(description = "主表：岗位表")
public class BasePostDO extends TempEntityTree<BasePostDO> {

    @Schema(description = "岗位名")
    private String name;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "该岗位的 uuid，备注：不能重复")
    private String uuid;

}