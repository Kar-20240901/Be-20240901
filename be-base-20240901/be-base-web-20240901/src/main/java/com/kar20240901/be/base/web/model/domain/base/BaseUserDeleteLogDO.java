package com.kar20240901.be.base.web.model.domain.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_user_delete_log")
@Data
@Schema(description = "子表：用户注销记录表，主表：用户表")
public class BaseUserDeleteLogDO extends TempEntityNoIdSuper {

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "密码，可为空，如果为空，则登录时需要提示【进行忘记密码操作】")
    private String password;

    @Schema(description = "邮箱，可以为空")
    private String email;

    @Schema(description = "用户名，可以为空")
    private String username;

    @Schema(description = "手机号，可以为空")
    private String phone;

    @Schema(description = "微信 openId，可以为空")
    private String wxOpenId;

    @Schema(description = "微信 appId，可以为空")
    private String wxAppId;

    @Schema(description = "该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不允许修改")
    private String uuid;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像 fileId（文件主键 id）")
    private Long avatarFileId;

}
