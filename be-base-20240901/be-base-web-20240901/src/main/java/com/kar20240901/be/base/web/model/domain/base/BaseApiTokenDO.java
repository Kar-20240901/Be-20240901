package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "base_api_token")
@Schema(description = "主表：apiToken")
public class BaseApiTokenDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户 id")
    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "调用 api时，传递的 token，格式：uuid")
    private String token;

    @Schema(description = "apiToken名")
    private String name;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "最近使用时间")
    private Date lastUseTime;

}