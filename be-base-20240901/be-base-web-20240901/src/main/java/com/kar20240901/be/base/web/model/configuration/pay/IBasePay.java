package com.kar20240901.be.base.web.model.configuration.pay;

import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayType;
import org.jetbrains.annotations.NotNull;

public interface IBasePay {

    /**
     * 支付方式类型
     */
    @NotNull
    IBasePayType getBasePayType();

    /**
     * 支付返回值，备注：一般返回 url
     */
    @NotNull
    BasePayReturnBO pay(PayDTO dto);

    /**
     * 查询订单状态
     */
    @NotNull
    BasePayTradeStatusEnum query(String outTradeNo, BasePayConfigurationDO basePayConfigurationDO);

}
