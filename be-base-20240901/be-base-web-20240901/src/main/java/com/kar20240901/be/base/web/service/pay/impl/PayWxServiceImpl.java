package com.kar20240901.be.base.web.service.pay.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.func.Func1;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.service.pay.PayWxService;
import com.kar20240901.be.base.web.util.pay.BasePayHelper;
import com.kar20240901.be.base.web.util.pay.BasePayUtil;
import com.kar20240901.be.base.web.util.pay.PayWxUtil;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayWxServiceImpl implements PayWxService {

    /**
     * 通用的处理：回调参数
     */
    @SneakyThrows
    private void commonHandleNotifyCallBack(HttpServletRequest request,
        Func1<RequestParam, BasePayTradeNotifyBO> func1) {

        String signature = request.getHeader("Wechatpay-Signature");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String serial = request.getHeader("Wechatpay-Serial");
        String signatureType = request.getHeader("Wechatpay-Signature-Type");

        ServletInputStream inputStream = request.getInputStream();

        String body = IoUtil.readUtf8(inputStream);

        // 构造 RequestParam
        RequestParam requestParam =
            new RequestParam.Builder().serialNumber(serial).nonce(nonce).signature(signature).signType(signatureType)
                .timestamp(timestamp).body(body).build();

        // 调用方法，获取：订单状态
        BasePayTradeNotifyBO basePayTradeNotifyBO = func1.call(requestParam);

        // 处理：订单回调
        BasePayUtil.handleTradeNotify(basePayTradeNotifyBO, null);

    }

    /**
     * 服务器异步通知-native，备注：第三方应用调用
     */
    @Override
    @SneakyThrows
    public void notifyCallBackNative(HttpServletRequest request, HttpServletResponse response,
        long basePayConfigurationId) {

        BasePayConfigurationDO basePayConfigurationDO = BasePayHelper.getBasePayConfigurationDO(basePayConfigurationId);

        if (basePayConfigurationDO == null) {
            return;
        }

        RSAAutoCertificateConfig rsaAutoCertificateConfig =
            PayWxUtil.getRsaAutoCertificateConfig(basePayConfigurationDO);

        NotificationParser notificationParser = new NotificationParser(rsaAutoCertificateConfig);

        commonHandleNotifyCallBack(request, (requestParam) -> {

            // 以支付通知回调为例，验签、解密并转换成 Transaction
            com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction transaction =
                notificationParser.parse(requestParam,
                    com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction.class);

            BasePayTradeNotifyBO basePayTradeNotifyBO = new BasePayTradeNotifyBO();

            Integer payerTotal = transaction.getAmount().getPayerTotal(); // 微信这里的单位是：分

            String totalAmount = String.valueOf(payerTotal / 100);

            basePayTradeNotifyBO.setTradeStatus(transaction.getTradeState().name());
            basePayTradeNotifyBO.setOutTradeNo(transaction.getOutTradeNo());
            basePayTradeNotifyBO.setTradeNo(transaction.getTransactionId());
            basePayTradeNotifyBO.setTotalAmount(totalAmount);
            basePayTradeNotifyBO.setPayCurrency(transaction.getAmount().getPayerCurrency());

            return basePayTradeNotifyBO;

        });

    }

    /**
     * 服务器异步通知-jsApi，备注：第三方应用调用
     */
    @Override
    public void notifyCallBackJsApi(HttpServletRequest request, HttpServletResponse response,
        long basePayConfigurationId) {

        BasePayConfigurationDO basePayConfigurationDO = BasePayHelper.getBasePayConfigurationDO(basePayConfigurationId);

        if (basePayConfigurationDO == null) {
            return;
        }

        RSAAutoCertificateConfig rsaAutoCertificateConfig =
            PayWxUtil.getRsaAutoCertificateConfig(basePayConfigurationDO);

        NotificationParser notificationParser = new NotificationParser(rsaAutoCertificateConfig);

        commonHandleNotifyCallBack(request, (requestParam) -> {

            // 以支付通知回调为例，验签、解密并转换成 Transaction
            com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction transaction =
                notificationParser.parse(requestParam,
                    com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction.class);

            BasePayTradeNotifyBO basePayTradeNotifyBO = new BasePayTradeNotifyBO();

            Integer payerTotal = transaction.getAmount().getPayerTotal(); // 微信这里的单位是：分

            String totalAmount = String.valueOf(payerTotal / 100);

            basePayTradeNotifyBO.setTradeStatus(transaction.getTradeState().name());
            basePayTradeNotifyBO.setOutTradeNo(transaction.getOutTradeNo());
            basePayTradeNotifyBO.setTradeNo(transaction.getTransactionId());
            basePayTradeNotifyBO.setTotalAmount(totalAmount);
            basePayTradeNotifyBO.setPayCurrency(transaction.getAmount().getPayerCurrency());

            return basePayTradeNotifyBO;

        });

    }

}
