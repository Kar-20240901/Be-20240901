package com.kar20240901.be.base.web.service.pay.impl;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayGooglePayConsumeDTO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayGooglePaySuccessDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.service.pay.PayGoogleService;
import com.kar20240901.be.base.web.util.pay.PayGoogleUtil;
import com.kar20240901.be.base.web.util.pay.PayHelper;
import com.kar20240901.be.base.web.util.pay.PayUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayGoogleServiceImpl implements PayGoogleService {

    /**
     * 支付成功的回调，备注：由客户端调用
     */
    @Override
    public boolean paySuccess(BasePayGooglePaySuccessDTO dto) {

        BasePayConfigurationDO basePayConfigurationDO =
            PayHelper.getBasePayConfigurationDO(dto.getBasePayConfigurationId());

        if (basePayConfigurationDO == null) {
            return false;
        }

        BasePayTradeNotifyBO basePayTradeNotifyBO = new BasePayTradeNotifyBO();

        BasePayTradeStatusEnum basePayTradeStatusEnum =
            PayGoogleUtil.query(dto.getId().toString(), basePayTradeNotifyBO, basePayConfigurationDO);

        if (BasePayTradeStatusEnum.WAIT_BUYER_CONSUME.equals(basePayTradeStatusEnum) == false) {
            return false;
        }

        basePayTradeNotifyBO.setTradeStatus(CollUtil.getFirst(basePayTradeStatusEnum.getStatusSet()));
        basePayTradeNotifyBO.setOutTradeNo(dto.getId().toString());

        // 处理：订单回调
        return PayUtil.handleTradeNotify(basePayTradeNotifyBO, basePayDO -> {

            basePayDO.setToken(dto.getToken());

        });

    }

    /**
     * 支付核销的回调，备注：由客户端调用
     */
    @Override
    @SneakyThrows
    public boolean payConsume(BasePayGooglePayConsumeDTO dto) {

        BasePayConfigurationDO basePayConfigurationDO =
            PayHelper.getBasePayConfigurationDO(dto.getBasePayConfigurationId());

        if (basePayConfigurationDO == null) {
            return false;
        }

        BasePayTradeNotifyBO basePayTradeNotifyBO = new BasePayTradeNotifyBO();

        BasePayTradeStatusEnum basePayTradeStatusEnum =
            PayGoogleUtil.query(dto.getId().toString(), basePayTradeNotifyBO, basePayConfigurationDO);

        if (BasePayTradeStatusEnum.TRADE_FINISHED.equals(basePayTradeStatusEnum) == false) {
            return false;
        }

        basePayTradeNotifyBO.setTradeStatus(CollUtil.getFirst(basePayTradeStatusEnum.getStatusSet()));
        basePayTradeNotifyBO.setOutTradeNo(dto.getId().toString());

        // 处理：订单回调
        return PayUtil.handleTradeNotify(basePayTradeNotifyBO, null);

    }

}
