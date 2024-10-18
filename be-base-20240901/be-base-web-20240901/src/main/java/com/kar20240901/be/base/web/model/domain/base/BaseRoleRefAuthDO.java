package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_role_ref_auth")
@Data
@Schema(description = "关联表：角色表，权限表")
public class BaseRoleRefAuthDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "角色主键id")
    private Long roleId;

    @Schema(description = "权限主键id")
    private Long authId;

}