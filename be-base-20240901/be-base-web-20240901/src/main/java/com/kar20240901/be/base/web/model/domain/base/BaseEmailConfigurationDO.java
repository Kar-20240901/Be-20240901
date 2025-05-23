package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_email_configuration")
@Data
@Schema(description = "主表：邮箱配置表")
public class BaseEmailConfigurationDO {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "正文前缀")
    private String contentPre;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "发送人邮箱")
    private String fromEmail;

    @Schema(description = "发送人密码")
    private String pass;

    @Schema(description = " 是否使用：SSL")
    private Boolean sslFlag;

}
