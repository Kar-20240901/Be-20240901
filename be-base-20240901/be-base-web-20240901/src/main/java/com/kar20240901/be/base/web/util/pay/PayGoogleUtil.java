package com.kar20240901.be.base.web.util.pay;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.bo.pay.BasePayGooglePurchasesBO;
import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.pay.BasePayService;
import java.time.Duration;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * 支付：谷歌工具类
 */
@Component
public class PayGoogleUtil {

    private static BasePayService basePayService;

    @Resource
    public void setBasePayService(BasePayService basePayService) {
        PayGoogleUtil.basePayService = basePayService;
    }

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        PayGoogleUtil.redissonClient = redissonClient;
    }

    /**
     * 支付
     */
    @SneakyThrows
    @NotNull
    public static BasePayReturnBO pay(PayDTO dto) {

        BasePayConfigurationDO basePayConfigurationDO = dto.getBasePayConfigurationDO();

        return new BasePayReturnBO(basePayConfigurationDO.getId().toString(), basePayConfigurationDO.getAppId());

    }

    /**
     * 查询订单状态
     *
     * @param outTradeNo 本系统的支付主键 id，必填
     */
    @SneakyThrows
    @NotNull
    public static BasePayTradeStatusEnum query(String outTradeNo, @Nullable BasePayTradeNotifyBO basePayTradeNotifyBO,
        BasePayConfigurationDO basePayConfigurationDO) {

        Assert.notBlank(outTradeNo);

        BasePayDO basePayDO = basePayService.lambdaQuery().eq(BasePayDO::getId, outTradeNo)
            .select(BasePayDO::getPackageName, BasePayDO::getProductId, BasePayDO::getToken,
                BasePayDO::getOriginalPrice).one();

        if (basePayDO == null) {
            R.error("谷歌支付查询失败：本系统不存在该支付", outTradeNo);
        }

        // 获取：accessToken
        String accessToken = getAccessToken(basePayConfigurationDO);

        // 查询：谷歌那边的订单状态，文档地址：https://developers.google.com/android-publisher/api-ref/rest/v3/purchases.products/get?hl=zh-cn
        // https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{packageName}/purchases/products/{productId}/tokens/{token}
        String url = StrUtil.format(
            "https://androidpublisher.googleapis.com/androidpublisher/v3/applications/{}/purchases/products/{}/tokens/{}",
            basePayDO.getPackageName(), basePayDO.getProductId(), basePayDO.getToken());

        String body = HttpRequest.get(url).header("Authorization", "Bearer " + accessToken).execute().body();

        BasePayGooglePurchasesBO basePayGooglePurchasesBO = JSONUtil.toBean(body, BasePayGooglePurchasesBO.class);

        String orderId = basePayGooglePurchasesBO.getOrderId();

        if (StrUtil.isBlank(orderId)) {
            R.error("谷歌支付查询失败：订单不存在", outTradeNo);
        }

        if (basePayTradeNotifyBO != null) {

            basePayTradeNotifyBO.setTradeNo(orderId);
            basePayTradeNotifyBO.setTotalAmount(basePayDO.getOriginalPrice().toPlainString()); // 备注：官方暂时没有返回实际支付金额的字段
            basePayTradeNotifyBO.setPayCurrency("");

        }

        if (basePayGooglePurchasesBO.getConsumptionState() != null
            && basePayGooglePurchasesBO.getConsumptionState() == 1) {

            return BasePayTradeStatusEnum.TRADE_FINISHED;

        }

        if (basePayGooglePurchasesBO.getPurchaseState() != null && basePayGooglePurchasesBO.getPurchaseState() == 0) {

            return BasePayTradeStatusEnum.WAIT_BUYER_CONSUME;

        }

        return BasePayTradeStatusEnum.WAIT_BUYER_PAY;

    }

    /**
     * 获取：google接口调用凭据
     */
    public static String getAccessToken(BasePayConfigurationDO basePayConfigurationDO) {

        String appId = basePayConfigurationDO.getAppId();

        RBucket<String> rBucket = redissonClient.getBucket(BaseRedisKeyEnum.GOOGLE_ACCESS_TOKEN_CACHE + ":" + appId);

        String accessToken = rBucket.get();

        if (StrUtil.isNotBlank(accessToken)) {
            return accessToken;
        }

        JSONObject formJson = JSONUtil.createObj();

        formJson.set("grant_type", "refresh_token");
        formJson.set("client_id", appId);
        formJson.set("client_secret", basePayConfigurationDO.getPrivateKey());
        formJson.set("refresh_token", basePayConfigurationDO.getPlatformPublicKey());

        String jsonStr = HttpUtil.post("https://accounts.google.com/o/oauth2/token", formJson);

        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

        String accessTokenResult = jsonObject.getStr("access_token");

        if (StrUtil.isBlank(accessTokenResult)) {

            R.error("谷歌：获取【access_token】失败，请联系管理员", jsonStr);

        }

        Long expiresIn = jsonObject.getLong("expires_in"); // 这里的单位是：秒

        rBucket.set(accessTokenResult, Duration.ofSeconds(expiresIn));

        return accessTokenResult;

    }

}
