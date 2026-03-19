package com.kar20240901.be.base.web.util.pay;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

/**
 * 支付：苹果工具类
 */
@Component
public class PayApplyUtil {

    public static final String OUT_TRADE_NO = "outTradeNo";

    /**
     * 支付
     */
    @SneakyThrows
    @NotNull
    public static BasePayReturnBO pay(PayDTO dto) {

        JSONObject jsonObject = JSONUtil.createObj().set(OUT_TRADE_NO, dto.getOutTradeNo());

        return new BasePayReturnBO(jsonObject.toString(), null);

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

        return BasePayTradeStatusEnum.UNKNOWN;

    }

}
