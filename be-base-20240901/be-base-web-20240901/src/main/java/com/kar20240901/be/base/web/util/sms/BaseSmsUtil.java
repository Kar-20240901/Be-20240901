package com.kar20240901.be.base.web.util.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.model.bo.sms.BaseSmsSendBO;
import com.kar20240901.be.base.web.model.configuration.sms.IBaseSms;
import com.kar20240901.be.base.web.model.vo.base.R;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信工具类
 */
@Component
public class BaseSmsUtil {

    private static final Map<Integer, IBaseSms> BASE_SMS_MAP = MapUtil.newHashMap();

    public BaseSmsUtil(@Autowired(required = false) @Nullable List<IBaseSms> iBaseSmsList) {

        if (CollUtil.isNotEmpty(iBaseSmsList)) {

            for (IBaseSms item : iBaseSmsList) {

                BASE_SMS_MAP.put(item.getBaseSmsType().getCode(), item);

            }

        }

    }

    /**
     * 执行发送
     */
    public static void send(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.send(baseSmsSendBO);

    }

    /**
     * 发送：验证码相关
     */
    public static void sendForCode(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendForCode(baseSmsSendBO);

    }

    /**
     * 发送：注册短信
     */
    public static void sendSignUp(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSignUp(baseSmsSendBO);

    }

    /**
     * 发送：登录短信
     */
    public static void sendSignIn(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSignIn(baseSmsSendBO);

    }

    /**
     * 发送：设置密码
     */
    public static void sendSetPassword(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSetPassword(baseSmsSendBO);

    }

    /**
     * 发送：修改密码
     */
    public static void sendUpdatePassword(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendUpdatePassword(baseSmsSendBO);

    }

    /**
     * 发送：设置用户名
     */
    public static void sendSetSignInName(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSetSignInName(baseSmsSendBO);

    }

    /**
     * 发送：修改用户名
     */
    public static void sendUpdateSignInName(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendUpdateSignInName(baseSmsSendBO);

    }

    /**
     * 发送：设置邮箱
     */
    public static void sendSetEmail(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSetEmail(baseSmsSendBO);

    }

    /**
     * 发送：修改邮箱
     */
    public static void sendUpdateEmail(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendUpdateEmail(baseSmsSendBO);

    }

    /**
     * 发送：设置微信
     */
    public static void sendSetWx(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSetWx(baseSmsSendBO);

    }

    /**
     * 发送：修改微信
     */
    public static void sendUpdateWx(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendUpdateWx(baseSmsSendBO);

    }

    /**
     * 发送：设置手机
     */
    public static void sendSetPhone(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSetPhone(baseSmsSendBO);

    }

    /**
     * 发送：修改手机
     */
    public static void sendUpdatePhone(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendUpdatePhone(baseSmsSendBO);

    }

    /**
     * 发送：忘记密码
     */
    public static void sendForgetPassword(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendForgetPassword(baseSmsSendBO);

    }

    /**
     * 发送：账号注销
     */
    public static void sendSignDelete(BaseSmsSendBO baseSmsSendBO) {

        IBaseSms iBaseSms = getIbaseSms(baseSmsSendBO);

        // 执行
        iBaseSms.sendSignDelete(baseSmsSendBO);

    }

    /**
     * 获取：短信实现类
     */
    @NotNull
    public static IBaseSms getIbaseSms(BaseSmsSendBO baseSmsSendBO) {

        if (StrUtil.isBlank(baseSmsSendBO.getPhoneNumber())) {

            R.error(BaseBizCodeEnum.THERE_IS_NO_BOUND_MOBILE_PHONE_NUMBER_SO_THIS_OPERATION_CANNOT_BE_PERFORMED);

        }

        BaseSmsHelper.handleBaseSmsConfigurationDO(baseSmsSendBO);

        // 执行：获取
        return doGetIbaseSms(baseSmsSendBO);

    }

    /**
     * 获取：短信实现类
     */
    @NotNull
    public static IBaseSms doGetIbaseSms(BaseSmsSendBO baseSmsSendBO) {

        if (StrUtil.isBlank(baseSmsSendBO.getSendContent())) {
            R.errorMsg("操作失败：发送内容不能为空");
        }

        Integer smsType = baseSmsSendBO.getBaseSmsConfigurationDO().getType();

        IBaseSms iBaseSms = BASE_SMS_MAP.get(smsType);

        if (iBaseSms == null) {
            R.errorMsg("操作失败：短信方式未找到：{}", smsType);
        }

        return iBaseSms;

    }

}
