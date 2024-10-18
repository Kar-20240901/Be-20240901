package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_auth")
@Data
@Schema(description = "主表：权限表")
public class BaseAuthDO extends TempEntity {

    @Schema(description = "角色名（不能重复）")
    private String name;

    @Schema(description = "权限，例子：base:menu:edit")
    private String auth;

}