package com.kar20240901.be.base.web.model.configuration.sms;

import com.kar20240901.be.base.web.model.bo.sms.BaseSmsSendBO;
import com.kar20240901.be.base.web.model.interfaces.sms.IBaseSmsType;

public interface IBaseSms {

    /**
     * 短信类型
     */
    IBaseSmsType getBaseSmsType();

    /**
     * 执行发送
     */
    void send(BaseSmsSendBO baseSmsSendBO);

    /**
     * 发送：验证码相关
     */
    void sendForCode(BaseSmsSendBO baseSmsSendBO);

    /**
     * 发送：注册短信
     */
    default void sendSignUp(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSignUp());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：登录短信
     */
    default void sendSignIn(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSignIn());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：设置密码
     */
    default void sendSetPassword(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSetPassword());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：修改密码
     */
    default void sendUpdatePassword(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendUpdatePassword());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：设置用户名
     */
    default void sendSetSignInName(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSetUserName());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：修改用户名
     */
    default void sendUpdateSignInName(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendUpdateUserName());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：设置邮箱
     */
    default void sendSetEmail(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSetEmail());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：修改邮箱
     */
    default void sendUpdateEmail(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendUpdateEmail());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：设置微信
     */
    default void sendSetWx(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSetWx());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：修改微信
     */
    default void sendUpdateWx(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendUpdateWx());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：设置手机
     */
    default void sendSetPhone(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSetPhone());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：修改手机
     */
    default void sendUpdatePhone(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendUpdatePhone());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：忘记密码
     */
    default void sendForgetPassword(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendForgetPassword());

        sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：账号注销
     */
    default void sendSignDelete(BaseSmsSendBO baseSmsSendBO) {

        baseSmsSendBO.setTemplateId(baseSmsSendBO.getBaseSmsConfigurationDO().getSendSignDelete());

        sendForCode(baseSmsSendBO);

    }

}
