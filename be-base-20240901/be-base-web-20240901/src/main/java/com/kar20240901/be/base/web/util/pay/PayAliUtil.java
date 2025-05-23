package com.kar20240901.be.base.web.util.pay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.BooleanUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.util.base.MyNumberUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 支付：支付宝工具类
 */
@Component
public class PayAliUtil {

    /**
     * 获取：支付相关配置对象
     */
    @NotNull
    public static AlipayConfig getAlipayConfig(BasePayConfigurationDO basePayConfigurationDO) {

        AlipayConfig alipayConfig = new AlipayConfig();

        alipayConfig.setServerUrl(basePayConfigurationDO.getServerUrl());
        alipayConfig.setAppId(basePayConfigurationDO.getAppId());
        alipayConfig.setPrivateKey(basePayConfigurationDO.getPrivateKey());
        alipayConfig.setAlipayPublicKey(basePayConfigurationDO.getPlatformPublicKey());

        return alipayConfig;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoPayBO {

        private BasePayConfigurationDO basePayConfigurationDO;

        private AlipayClient alipayClient;

        private String notifyUrl;

    }

    /**
     * 通用的，执行支付
     */
    @SneakyThrows
    private static BasePayReturnBO doPay(PayDTO dto, Func1<DoPayBO, BasePayReturnBO> func1) {

        BasePayConfigurationDO basePayConfigurationDO = dto.getBasePayConfigurationDO();

        AlipayConfig alipayConfig = getAlipayConfig(basePayConfigurationDO);

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        String notifyUrl = basePayConfigurationDO.getNotifyUrl() + "/" + basePayConfigurationDO.getId();

        // 执行支付
        return func1.call(new DoPayBO(basePayConfigurationDO, alipayClient, notifyUrl));

    }

    /**
     * 当面付：二维码扫描付款 参考地址：https://open.alipay.com/api/apiDebug
     */
    @SneakyThrows
    @NotNull
    public static BasePayReturnBO payQrCode(PayDTO dto) {

        // 执行
        return doPay(dto, doPayBO -> {

            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

            model.setOutTradeNo(dto.getOutTradeNo());
            model.setTotalAmount(MyNumberUtil.getStr(dto.getTotalAmount()));
            model.setSubject(dto.getSubject());
            model.setBody(dto.getBody());
            model.setTimeExpire(DateUtil.formatDateTime(dto.getExpireTime()));

            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

            request.setNotifyUrl(doPayBO.getNotifyUrl()); // 设置：异步通知地址
            request.setBizModel(model);

            AlipayTradePrecreateResponse response = doPayBO.getAlipayClient().execute(request);

            // 处理：支付宝的返回值
            handleApiPayResponse(response.isSuccess(), "支付宝支付失败：", response.getSubMsg());

            // 返回：扫码地址
            return new BasePayReturnBO(response.getQrCode(), doPayBO.getBasePayConfigurationDO().getAppId());

        });

    }

    /**
     * 支付宝-手机支付
     */
    @SneakyThrows
    public static BasePayReturnBO payApp(PayDTO dto) {

        // 执行
        return doPay(dto, doPayBO -> {

            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

            model.setOutTradeNo(dto.getOutTradeNo());
            model.setTotalAmount(MyNumberUtil.getStr(dto.getTotalAmount()));
            model.setSubject(dto.getSubject());
            model.setBody(dto.getBody());
            model.setTimeExpire(DateUtil.formatDateTime(dto.getExpireTime()));

            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

            request.setNotifyUrl(doPayBO.getNotifyUrl()); // 设置：异步通知地址
            request.setBizModel(model);

            AlipayTradeAppPayResponse response = doPayBO.getAlipayClient().execute(request);

            // 处理：支付宝的返回值
            handleApiPayResponse(response.isSuccess(), "支付宝支付失败：", response.getSubMsg());

            // 返回：调用手机支付需要的参数
            return new BasePayReturnBO(response.getBody(), doPayBO.getBasePayConfigurationDO().getAppId());

        });

    }

    /**
     * 支付宝-电脑网站支付
     */
    @SneakyThrows
    public static BasePayReturnBO payWebPc(PayDTO dto) {

        // 执行
        return doPay(dto, doPayBO -> {

            AlipayTradePagePayModel model = new AlipayTradePagePayModel();

            model.setOutTradeNo(dto.getOutTradeNo());
            model.setTotalAmount(MyNumberUtil.getStr(dto.getTotalAmount()));
            model.setSubject(dto.getSubject());
            model.setBody(dto.getBody());
            model.setTimeExpire(DateUtil.formatDateTime(dto.getExpireTime()));

            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

            request.setNotifyUrl(doPayBO.getNotifyUrl()); // 设置：异步通知地址
            request.setBizModel(model);

            // 备注：指定为 GET，那么 body就是 url，反之就是：html的 form表单格式
            AlipayTradePagePayResponse response = doPayBO.getAlipayClient().pageExecute(request, "GET");

            // 处理：支付宝的返回值
            handleApiPayResponse(response.isSuccess(), "支付宝支付失败：", response.getSubMsg());

            // 返回：支付的 url链接
            return new BasePayReturnBO(response.getBody(), doPayBO.getBasePayConfigurationDO().getAppId());

        });

    }

    /**
     * 支付宝-手机网站支付
     */
    @SneakyThrows
    public static BasePayReturnBO payWebApp(PayDTO dto) {

        // 执行
        return doPay(dto, doPayBO -> {

            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

            model.setOutTradeNo(dto.getOutTradeNo());
            model.setTotalAmount(MyNumberUtil.getStr(dto.getTotalAmount()));
            model.setSubject(dto.getSubject());
            model.setBody(dto.getBody());
            model.setTimeExpire(DateUtil.formatDateTime(dto.getExpireTime()));

            model.setProductCode("QUICK_WAP_WAY");

            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();

            request.setNotifyUrl(doPayBO.getNotifyUrl()); // 设置：异步通知地址
            request.setBizModel(model);

            // 备注：指定为 GET，那么 body就是 url，反之就是：html的 form表单格式
            AlipayTradeWapPayResponse response = doPayBO.getAlipayClient().pageExecute(request, "GET");

            // 处理：支付宝的返回值
            handleApiPayResponse(response.isSuccess(), "支付宝支付失败：", response.getSubMsg());

            // 返回：支付的 url链接
            return new BasePayReturnBO(response.getBody(), doPayBO.getBasePayConfigurationDO().getAppId());

        });

    }

    /**
     * 处理：支付宝的返回值
     */
    private static void handleApiPayResponse(boolean success, String preMsg, String subMsg) {

        if (BooleanUtil.isFalse(success)) {

            // code，例如：40004
            // msg，例如：Business Failed
            // sub_code，例如：ACQ.TRADE_HAS_SUCCESS
            // sub_msg，例如：交易已被支付
            R.errorMsg(preMsg + subMsg);

        }

    }

    /**
     * 通用的，查询订单状态
     *
     * @param outTradeNo 本系统的支付主键 id，必填
     */
    @SneakyThrows
    @NotNull
    public static BasePayTradeStatusEnum query(String outTradeNo, BasePayConfigurationDO basePayConfigurationDO) {

        Assert.notBlank(outTradeNo);

        AlipayConfig alipayConfig = getAlipayConfig(basePayConfigurationDO);

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        AlipayTradeQueryModel model = new AlipayTradeQueryModel();

        model.setOutTradeNo(outTradeNo);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        request.setBizModel(model);

        AlipayTradeQueryResponse response = alipayClient.execute(request);

        // 处理：支付宝的返回值
        handleApiPayResponse(response.isSuccess(), "支付宝查询失败：", response.getSubMsg());

        return BasePayTradeStatusEnum.getByStatus(response.getTradeStatus());

    }

}
