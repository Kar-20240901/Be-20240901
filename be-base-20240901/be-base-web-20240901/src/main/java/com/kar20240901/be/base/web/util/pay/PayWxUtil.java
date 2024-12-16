package com.kar20240901.be.base.web.util.pay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.util.GsonUtil;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 支付：微信工具类
 */
@Component
public class PayWxUtil {

    /**
     * 获取：RSAAutoCertificateConfig 对象
     */
    public static RSAAutoCertificateConfig getRsaAutoCertificateConfig(BasePayConfigurationDO basePayConfigurationDO) {

        return new RSAAutoCertificateConfig.Builder().merchantId(basePayConfigurationDO.getMerchantId())
            .privateKey(basePayConfigurationDO.getPrivateKey())
            .merchantSerialNumber(basePayConfigurationDO.getMerchantSerialNumber())
            .apiV3Key(basePayConfigurationDO.getApiV3Key()).build();

    }

    /**
     * 获取：NativePayService 对象
     */
    private static NativePayService getNativePayService(BasePayConfigurationDO basePayConfigurationDO) {

        RSAAutoCertificateConfig rsaAutoCertificateConfig = getRsaAutoCertificateConfig(basePayConfigurationDO);

        return new NativePayService.Builder().config(rsaAutoCertificateConfig).build();

    }

    /**
     * 支付-native
     */
    @SneakyThrows
    @NotNull
    public static BasePayReturnBO payNative(PayDTO dto) {

        BasePayConfigurationDO basePayConfigurationDO = dto.getBasePayConfigurationDO();

        NativePayService nativePayService = getNativePayService(basePayConfigurationDO);

        com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest request =
            new com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest();

        com.wechat.pay.java.service.payments.nativepay.model.Amount amount =
            new com.wechat.pay.java.service.payments.nativepay.model.Amount();

        amount.setTotal(dto.getTotalAmount().multiply(TempConstant.BIG_DECIMAL_ONE_HUNDRED).intValue());

        request.setAmount(amount);

        request.setAppid(basePayConfigurationDO.getAppId());
        request.setMchid(basePayConfigurationDO.getMerchantId());
        request.setDescription(dto.getSubject());

        request.setNotifyUrl(basePayConfigurationDO.getNotifyUrl() + "/" + basePayConfigurationDO.getId());

        request.setOutTradeNo(dto.getOutTradeNo());
        request.setTimeExpire(DatePattern.UTC_WITH_XXX_OFFSET_FORMAT.format(dto.getExpireTime()));

        // 调用接口
        com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse prepayResponse =
            nativePayService.prepay(request);

        // 返回：扫码的二维码地址
        return new BasePayReturnBO(prepayResponse.getCodeUrl(), basePayConfigurationDO.getAppId());

    }

    /**
     * 查询订单状态-native
     *
     * @param outTradeNo 本系统的支付主键 id，必填
     */
    @SneakyThrows
    @NotNull
    public static BasePayTradeStatusEnum queryNative(String outTradeNo, BasePayConfigurationDO basePayConfigurationDO) {

        NativePayService nativePayService = getNativePayService(basePayConfigurationDO);

        com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest queryRequest =
            new com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest();

        queryRequest.setMchid(basePayConfigurationDO.getMerchantId());
        queryRequest.setOutTradeNo(outTradeNo);

        // 调用接口
        Transaction transaction = nativePayService.queryOrderByOutTradeNo(queryRequest);

        return BasePayTradeStatusEnum.getByStatus(transaction.getTradeState().name());

    }

    /**
     * 获取：JsapiServiceExtension 对象
     */
    private static JsapiServiceExtension getJsapiServiceExtension(BasePayConfigurationDO basePayConfigurationDO) {

        RSAAutoCertificateConfig rsaAutoCertificateConfig = getRsaAutoCertificateConfig(basePayConfigurationDO);

        return new JsapiServiceExtension.Builder().config(rsaAutoCertificateConfig).build();

    }

    /**
     * 支付-jsApi：jsApi调起支付需要的参数
     */
    @SneakyThrows
    @NotNull
    public static BasePayReturnBO payJsApi(PayDTO dto) {

        Assert.notBlank(dto.getOpenId());

        BasePayConfigurationDO basePayConfigurationDO = dto.getBasePayConfigurationDO();

        JsapiServiceExtension jsapiServiceExtension = getJsapiServiceExtension(basePayConfigurationDO);

        com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest request =
            new com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest();

        com.wechat.pay.java.service.payments.jsapi.model.Amount amount =
            new com.wechat.pay.java.service.payments.jsapi.model.Amount();

        amount.setTotal(dto.getTotalAmount().multiply(TempConstant.BIG_DECIMAL_ONE_HUNDRED).intValue());

        request.setAmount(amount);

        request.setAppid(basePayConfigurationDO.getAppId());
        request.setMchid(basePayConfigurationDO.getMerchantId());
        request.setDescription(dto.getSubject());

        request.setNotifyUrl(basePayConfigurationDO.getNotifyUrl() + "/" + basePayConfigurationDO.getId());

        request.setOutTradeNo(dto.getOutTradeNo());
        request.setTimeExpire(DatePattern.UTC_WITH_XXX_OFFSET_FORMAT.format(dto.getExpireTime()));

        com.wechat.pay.java.service.payments.jsapi.model.Payer payer =
            new com.wechat.pay.java.service.payments.jsapi.model.Payer();

        payer.setOpenid(dto.getOpenId());

        request.setPayer(payer);

        // 执行
        PrepayWithRequestPaymentResponse prepayWithRequestPaymentResponse =
            jsapiServiceExtension.prepayWithRequestPayment(request);

        String jsonStr = GsonUtil.toJson(prepayWithRequestPaymentResponse);

        return new BasePayReturnBO(jsonStr, basePayConfigurationDO.getAppId());

    }

    /**
     * 查询订单状态-jsApi
     *
     * @param outTradeNo 本系统的支付主键 id，必填
     */
    @SneakyThrows
    @NotNull
    public static BasePayTradeStatusEnum queryJsApi(String outTradeNo, BasePayConfigurationDO basePayConfigurationDO) {

        JsapiServiceExtension jsapiServiceExtension = getJsapiServiceExtension(basePayConfigurationDO);

        com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest queryRequest =
            new com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest();

        queryRequest.setMchid(basePayConfigurationDO.getMerchantId());
        queryRequest.setOutTradeNo(outTradeNo);

        // 调用接口
        Transaction transaction = jsapiServiceExtension.queryOrderByOutTradeNo(queryRequest);

        return BasePayTradeStatusEnum.getByStatus(transaction.getTradeState().name());

    }

    /**
     * 获取：H5Service 对象
     */
    private static H5Service getH5Service(BasePayConfigurationDO basePayConfigurationDO) {

        RSAAutoCertificateConfig rsaAutoCertificateConfig = getRsaAutoCertificateConfig(basePayConfigurationDO);

        return new H5Service.Builder().config(rsaAutoCertificateConfig).build();

    }

    /**
     * 支付-h5
     */
    @SneakyThrows
    @NotNull
    public static BasePayReturnBO payH5(PayDTO dto) {

        BasePayConfigurationDO basePayConfigurationDO = dto.getBasePayConfigurationDO();

        H5Service h5Service = getH5Service(basePayConfigurationDO);

        com.wechat.pay.java.service.payments.h5.model.PrepayRequest request =
            new com.wechat.pay.java.service.payments.h5.model.PrepayRequest();

        com.wechat.pay.java.service.payments.h5.model.Amount amount =
            new com.wechat.pay.java.service.payments.h5.model.Amount();

        amount.setTotal(dto.getTotalAmount().multiply(TempConstant.BIG_DECIMAL_ONE_HUNDRED).intValue());

        request.setAmount(amount);

        request.setAppid(basePayConfigurationDO.getAppId());
        request.setMchid(basePayConfigurationDO.getMerchantId());
        request.setDescription(dto.getSubject());

        request.setNotifyUrl(basePayConfigurationDO.getNotifyUrl() + "/" + basePayConfigurationDO.getId());

        request.setOutTradeNo(dto.getOutTradeNo());
        request.setTimeExpire(DatePattern.UTC_WITH_XXX_OFFSET_FORMAT.format(dto.getExpireTime()));

        // 调用接口
        com.wechat.pay.java.service.payments.h5.model.PrepayResponse prepayResponse = h5Service.prepay(request);

        // 返回：支付跳转的地址
        return new BasePayReturnBO(prepayResponse.getH5Url(), basePayConfigurationDO.getAppId());

    }

    /**
     * 查询订单状态-h5
     *
     * @param outTradeNo 本系统的支付主键 id，必填
     */
    @SneakyThrows
    @NotNull
    public static BasePayTradeStatusEnum queryH5(String outTradeNo, BasePayConfigurationDO basePayConfigurationDO) {

        H5Service h5Service = getH5Service(basePayConfigurationDO);

        com.wechat.pay.java.service.payments.h5.model.QueryOrderByOutTradeNoRequest queryRequest =
            new com.wechat.pay.java.service.payments.h5.model.QueryOrderByOutTradeNoRequest();

        queryRequest.setMchid(basePayConfigurationDO.getMerchantId());
        queryRequest.setOutTradeNo(outTradeNo);

        // 调用接口
        Transaction transaction = h5Service.queryOrderByOutTradeNo(queryRequest);

        return BasePayTradeStatusEnum.getByStatus(transaction.getTradeState().name());

    }

}
