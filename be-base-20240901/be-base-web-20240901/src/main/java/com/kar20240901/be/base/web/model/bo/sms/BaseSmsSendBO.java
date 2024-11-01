package com.kar20240901.be.base.web.model.bo.sms;

import com.kar20240901.be.base.web.model.domain.sms.BaseSmsConfigurationDO;
import com.kar20240901.be.base.web.model.interfaces.sms.IBaseSmsType;
import lombok.Data;

@Data
public class BaseSmsSendBO {

    /**
     * 需要发送的内容
     */
    private String sendContent;

    /**
     * 使用的配置文件
     */
    private BaseSmsConfigurationDO baseSmsConfigurationDO;

    /**
     * 模版 id
     */
    private String templateId;

    /**
     * 模版参数 set
     */
    private String[] templateParamSet;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信类型：101 阿里 201 腾讯
     * <p>
     * {@link IBaseSmsType}
     */
    private Integer smsType;

}
