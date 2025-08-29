package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.enums.base.BaseDictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_dict")
@Data
@Schema(description = "主表：字典表")
public class BaseDictDO extends TempEntity {

    @Schema(description = "字典 key（不能重复），备注：字典项要冗余这个 key，目的：方便操作")
    private String dictKey;

    @Schema(description = "字典/字典项 名")
    private String name;

    @Schema(description = "字典类型")
    private BaseDictTypeEnum type;

    @Schema(description = "字典项 value（数字 123...）备注：字典为 -1")
    private Integer value;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @TableField(exist = false)
    @Schema(description = "字典的子节点")
    private List<BaseDictDO> children;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "该字典的 uuid")
    private String uuid;

}
