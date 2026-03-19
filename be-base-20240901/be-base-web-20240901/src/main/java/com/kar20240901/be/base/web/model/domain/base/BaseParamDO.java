package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_param")
@Data
@Schema(description = "主表：参数表")
public class BaseParamDO extends TempEntity {

    @Schema(description = "配置名，备注：以 uuid为不变值进行使用，不要用此属性")
    private String name;

    @Schema(description = "值")
    private String value;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "该参数的 uuid，备注：系统内置参数的 uuid等于 id")
    private String uuid;

}
