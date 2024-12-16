package com.kar20240901.be.base.web.service.pay.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.service.pay.BasePayService;
import com.kar20240901.be.base.web.service.pay.PayApplyService;
import com.kar20240901.be.base.web.util.pay.PayApplyUtil;
import com.kar20240901.be.base.web.util.pay.PayUtil;
import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayApplyServiceImpl implements PayApplyService {

    @Resource
    BasePayService basePayService;

    /**
     * 服务器异步通知，备注：第三方应用调用
     */
    @Override
    public String notifyCallBack(JSONObject jsonObject) {

        String signedPayloadStr = jsonObject.getStr("signedPayload");

        if (StrUtil.isBlank(signedPayloadStr)) {

            throw new RuntimeException("signedPayload不能为空");

        }

        JSONObject signedPayloadJson = getPayloads(signedPayloadStr);

        String signedTransactionInfoStr = signedPayloadJson.getJSONObject("data").getStr("signedTransactionInfo");

        JSONObject signedTransactionInfoJson = getPayloads(signedTransactionInfoStr);

        String transactionReason = signedTransactionInfoJson.getStr("transactionReason");

        if (!"PURCHASE".equalsIgnoreCase(transactionReason)) {

            log.info("苹果支付，transactionReason 不是 PURCHASE：{}", transactionReason);
            return "success";

        }

        String appAccountToken = signedTransactionInfoJson.getStr("appAccountToken");

        JSONObject appAccountTokenJson = JSONUtil.parseObj(appAccountToken);

        // 获取：本系统订单 id
        String outTradeNo = appAccountTokenJson.getStr(PayApplyUtil.OUT_TRADE_NO);

        if (StrUtil.isBlank(outTradeNo)) {

            log.info("苹果支付，outTradeNo为空，appAccountTokenJson：{}", appAccountTokenJson);
            return "success";

        }

        BasePayDO basePayDO =
            basePayService.lambdaQuery().eq(BasePayDO::getId, outTradeNo).select(BasePayDO::getOriginalPrice).one();

        if (basePayDO == null) {

            log.info("苹果支付，basePayDO为 null：{}", outTradeNo);
            return "success";

        }

        BasePayTradeNotifyBO basePayTradeNotifyBO = new BasePayTradeNotifyBO();

        basePayTradeNotifyBO.setTradeStatus(CollUtil.getFirst(BasePayTradeStatusEnum.TRADE_SUCCESS.getStatusSet()));
        basePayTradeNotifyBO.setOutTradeNo(outTradeNo);
        basePayTradeNotifyBO.setTradeNo(TempConstant.NEGATIVE_ONE_STR);
        basePayTradeNotifyBO.setTotalAmount(basePayDO.getOriginalPrice().toPlainString());
        basePayTradeNotifyBO.setPayCurrency("CNY");

        // 处理：订单回调
        PayUtil.handleTradeNotify(basePayTradeNotifyBO, null);

        return "success";

    }

    @SneakyThrows
    private JSONObject getPayloads(String signedPayload) {

        JWT jwt = JWT.of(signedPayload);

        String x5cListStr = (String)jwt.getHeader("x5c");

        String x5c0 = JSONUtil.toList(x5cListStr, String.class).get(0);

        byte[] x5c0Bytes = java.util.Base64.getDecoder().decode(x5c0);

        CertificateFactory fact = CertificateFactory.getInstance("X.509");

        X509Certificate cer = (X509Certificate)fact.generateCertificate(new ByteArrayInputStream(x5c0Bytes));

        PublicKey publicKey = cer.getPublicKey();

        jwt.setKey(publicKey.getEncoded());

        // 验证 x5c证书
        jwt.verify();

        return jwt.getPayloads();

    }

}
