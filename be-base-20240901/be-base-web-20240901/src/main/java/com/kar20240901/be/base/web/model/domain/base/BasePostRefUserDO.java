package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "base_post_ref_user")
@Data
@Schema(description = "关联表：岗位表，用户表")
@NoArgsConstructor
@AllArgsConstructor
public class BasePostRefUserDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "岗位主键id")
    private Long postId;

    @Schema(description = "用户主键id")
    private Long userId;

}