package com.kar20240901.be.base.web.configuration.pay.wx;

import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.configuration.pay.IBasePay;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayType;
import com.kar20240901.be.base.web.util.pay.PayWxUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付相关配置类
 */
@Configuration
public class PayWxJsApiConfiguration implements IBasePay {

    /**
     * 支付方式类型
     */
    @Override
    @NotNull
    public IBasePayType getBasePayType() {
        return BasePayTypeEnum.WX_JSAPI;
    }

    /**
     * 支付返回值，备注：一般返回 url
     */
    @Override
    @NotNull
    public BasePayReturnBO pay(PayDTO dto) {
        return PayWxUtil.payJsApi(dto);
    }

    /**
     * 查询订单状态
     */
    @Override
    @NotNull
    public BasePayTradeStatusEnum query(String outTradeNo, BasePayConfigurationDO basePayConfigurationDO) {
        return PayWxUtil.queryJsApi(outTradeNo, basePayConfigurationDO);
    }

}
