package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "base_role_ref_user")
@Data
@Schema(description = "关联表：角色表，用户表")
@NoArgsConstructor
@AllArgsConstructor
public class BaseRoleRefUserDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "角色主键id")
    private Long roleId;

    @Schema(description = "用户主键id")
    private Long userId;

}