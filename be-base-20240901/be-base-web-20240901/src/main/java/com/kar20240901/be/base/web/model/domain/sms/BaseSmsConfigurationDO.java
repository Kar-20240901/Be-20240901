package com.kar20240901.be.base.web.model.domain.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.interfaces.sms.IBaseSmsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_sms_configuration")
@Data
@Schema(description = "主表：短信配置表")
public class BaseSmsConfigurationDO extends TempEntity {

    @Schema(description = "是否是默认短信发送，备注：只会有一个默认短信发送")
    private Boolean defaultFlag;

    /**
     * {@link IBaseSmsType}
     */
    @Schema(description = "短信类型：101 阿里 201 腾讯")
    private Integer type;

    @Schema(description = "短信名")
    private String name;

    @Schema(description = "钥匙")
    private String secretId;

    @Schema(description = "秘钥")
    private String secretKey;

    @Schema(description = "短信应用 id")
    private String sdkAppId;

    @Schema(description = "签名内容")
    private String signName;

    @Schema(description = "发送：通用短信")
    private String sendCommon;

    @Schema(description = "发送：注册短信")
    private String sendSignUp;

    @Schema(description = "发送：登录短信")
    private String sendSignIn;

    @Schema(description = "发送：设置密码")
    private String sendSetPassword;

    @Schema(description = "发送：修改密码")
    private String sendUpdatePassword;

    @Schema(description = "发送：设置用户名")
    private String sendSetUserName;

    @Schema(description = "发送：修改用户名")
    private String sendUpdateUserName;

    @Schema(description = "发送：设置邮箱")
    private String sendSetEmail;

    @Schema(description = "发送：修改邮箱")
    private String sendUpdateEmail;

    @Schema(description = "发送：设置微信")
    private String sendSetWx;

    @Schema(description = "发送：修改微信")
    private String sendUpdateWx;

    @Schema(description = "发送：设置手机")
    private String sendSetPhone;

    @Schema(description = "发送：修改手机")
    private String sendUpdatePhone;

    @Schema(description = "发送：忘记密码")
    private String sendForgetPassword;

    @Schema(description = "发送：账号注销")
    private String sendSignDelete;

}
