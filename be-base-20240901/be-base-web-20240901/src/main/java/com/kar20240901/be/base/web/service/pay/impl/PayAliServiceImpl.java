package com.kar20240901.be.base.web.service.pay.impl;

import com.alipay.api.AlipayConfig;
import com.alipay.api.internal.util.AlipaySignature;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.service.pay.PayAliService;
import com.kar20240901.be.base.web.util.pay.BasePayHelper;
import com.kar20240901.be.base.web.util.pay.BasePayUtil;
import com.kar20240901.be.base.web.util.pay.PayAliUtil;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayAliServiceImpl implements PayAliService {

    /**
     * 服务器异步通知，备注：第三方应用调用
     */
    @SneakyThrows
    @Override
    public String notifyCallBack(HttpServletRequest request, long basePayConfigurationId) {

        // 获取：支付的参数配置对象
        BasePayConfigurationDO basePayConfigurationDO = BasePayHelper.getBasePayConfigurationDO(basePayConfigurationId);

        if (basePayConfigurationDO == null) {
            return "success";
        }

        Map<String, String> paramsMap = new HashMap<>();

        Map<String, String[]> requestParamMap = request.getParameterMap();

        for (Iterator<String> iter = requestParamMap.keySet().iterator(); iter.hasNext(); ) {

            String name = iter.next();

            String[] values = requestParamMap.get(name);

            String valueStr = "";

            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";

            }

            paramsMap.put(name, valueStr);

        }

        AlipayConfig alipayConfig = PayAliUtil.getAlipayConfig(basePayConfigurationDO);

        boolean signVerified =
            AlipaySignature.rsaCheckV1(paramsMap, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(),
                alipayConfig.getSignType()); // 调用SDK验证签名

        if (signVerified) {

            // 商户订单号
            String outTradeNo = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            // 支付宝交易号
            String tradeNo = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            // 付款金额
            String totalAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            // 交易状态
            String tradeStatus = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8);

            BasePayTradeNotifyBO basePayTradeNotifyBO = new BasePayTradeNotifyBO();

            basePayTradeNotifyBO.setTradeStatus(tradeStatus);
            basePayTradeNotifyBO.setOutTradeNo(outTradeNo);
            basePayTradeNotifyBO.setTradeNo(tradeNo);
            basePayTradeNotifyBO.setTotalAmount(totalAmount);
            basePayTradeNotifyBO.setPayCurrency("CNY");

            // 处理：订单回调
            BasePayUtil.handleTradeNotify(basePayTradeNotifyBO, null);

        }

        return "success"; // 备注：这里一直都返回 success，原因：如果返回 failure，则支付宝那边会再次回调，没有这个必要

    }

}
