package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailException;
import cn.hutool.extra.mail.MailUtil;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.model.domain.base.BaseEmailConfigurationDO;
import com.kar20240901.be.base.web.model.enums.base.EmailMessageEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.base.BaseEmailConfigurationService;
import org.springframework.stereotype.Component;

/**
 * 邮箱工具类
 */
@Component
public class MyEmailUtil {

    private static BaseEmailConfigurationService baseEmailConfigurationService;

    public MyEmailUtil(BaseEmailConfigurationService baseEmailConfigurationService) {

        MyEmailUtil.baseEmailConfigurationService = baseEmailConfigurationService;

    }

    /**
     * 发送邮件
     */
    public static void send(String to, EmailMessageEnum emailMessageEnum, String content) {

        send(to, emailMessageEnum, content, false);

    }

    /**
     * 发送邮件
     */
    public static void send(String to, EmailMessageEnum emailMessageEnum, String content, boolean isHtml) {

        if (StrUtil.isBlank(to)) {

            R.error(BaseBizCodeEnum.THIS_OPERATION_CANNOT_BE_PERFORMED_WITHOUT_BINDING_AN_EMAIL_ADDRESS);

        }

        BaseEmailConfigurationDO baseEmailConfigurationDO = baseEmailConfigurationService.lambdaQuery().one();

        if (baseEmailConfigurationDO == null) {
            R.errorMsg("操作失败：未配置邮箱参数，请联系管理员");
        }

        // 消息内容，加上统一的前缀
        content = baseEmailConfigurationDO.getContentPre() + StrUtil.format(emailMessageEnum.getContentTemp(), content);

        String finalContent = content;

        MailAccount mailAccount = new MailAccount();

        mailAccount.setPort(baseEmailConfigurationDO.getPort());
        mailAccount.setFrom(baseEmailConfigurationDO.getFromEmail());
        mailAccount.setPass(baseEmailConfigurationDO.getPass());

        if (BooleanUtil.isTrue(baseEmailConfigurationDO.getSslFlag())) {

            mailAccount.setStarttlsEnable(true);
            mailAccount.setSslEnable(true);

        }

        try {

            MailUtil.send(mailAccount, to, emailMessageEnum.getSubject(), finalContent, isHtml);

        } catch (MailException e) {

            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {

                R.error(BaseBizCodeEnum.EMAIL_DOES_NOT_EXIST_PLEASE_RE_ENTER);

            } else {

                throw e;

            }

        }

    }

}
