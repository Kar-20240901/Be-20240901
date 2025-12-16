package com.kar20240901.be.base.web.util.pay;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.util.RandomUtil;
import com.kar20240901.be.base.web.model.bo.socket.BaseWebSocketStrEventBO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.dto.socket.WebSocketMessageDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTypeEnum;
import com.kar20240901.be.base.web.model.enums.socket.BaseWebSocketUriEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.pay.BasePayConfigurationService;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class PayHelper {

    private static BasePayConfigurationService basePayConfigurationService;

    @Resource
    public void setBasePayConfigurationService(BasePayConfigurationService basePayConfigurationService) {
        PayHelper.basePayConfigurationService = basePayConfigurationService;
    }

    // key：BasePayConfigurationId，value：客户端
    private static final ConcurrentHashMap<Long, Object> BASE_PAY_CLIENT_MAP = new ConcurrentHashMap<>();

    /**
     * 获取或者设置客户端
     */
    public static Object getOrSetBasePayClientMap(Long basePayConfigurationId, Func0<Object> getDefaultObject) {

        return BASE_PAY_CLIENT_MAP.computeIfAbsent(basePayConfigurationId,
            k -> getDefaultObject.callWithRuntimeException());

    }

    /**
     * 移除客户端
     */
    @SneakyThrows
    public static void clearByIdBasePayClientMap(Long basePayConfigurationId) {

        BASE_PAY_CLIENT_MAP.remove(basePayConfigurationId);

    }

    /**
     * 发送消息：关闭前端支付弹窗
     */
    public static void sendBasePayCloseModalTopic(BasePayDO basePayDO) {

        BaseWebSocketStrEventBO<Long> baseWebSocketStrEventBO = new BaseWebSocketStrEventBO<>();

        baseWebSocketStrEventBO.setUserIdSet(CollUtil.newHashSet(basePayDO.getUserId()));

        WebSocketMessageDTO<Long> webSocketMessageDTO =
            WebSocketMessageDTO.okData(BaseWebSocketUriEnum.BASE_PAY_CLOSE_MODAL, basePayDO.getId());

        baseWebSocketStrEventBO.setWebSocketMessageDTO(webSocketMessageDTO);

        // 发送：webSocket事件
        TempKafkaUtil.sendBaseWebSocketStrEventTopic(baseWebSocketStrEventBO);

    }

    /**
     * 获取：BasePayConfigurationDO对象
     */
    @NotNull
    public static BasePayConfigurationDO getBasePayConfigurationDO(Integer payType) {

        List<BasePayConfigurationDO> basePayConfigurationDOList =
            basePayConfigurationService.lambdaQuery().eq(BasePayConfigurationDO::getEnableFlag, true)
                .eq(BasePayConfigurationDO::getType, payType).list();

        BasePayConfigurationDO basePayConfigurationDO = null;

        if (CollUtil.isEmpty(basePayConfigurationDOList)) {

            if (BasePayTypeEnum.WX_JSAPI.getCode() == payType) {

                // 获取：当前微信二维码支付
                basePayConfigurationDOList =
                    basePayConfigurationService.lambdaQuery().eq(BasePayConfigurationDO::getEnableFlag, true)
                        .eq(BasePayConfigurationDO::getType, BasePayTypeEnum.WX_NATIVE.getCode()).list();

                if (CollUtil.isEmpty(basePayConfigurationDOList)) {

                    R.error("操作失败：暂未配置支付", payType);

                } else {

                    // 随机取一个
                    basePayConfigurationDO = RandomUtil.randomEle(basePayConfigurationDOList);

                    return basePayConfigurationDO;

                }

            }

            R.error("操作失败：暂未配置支付", payType);

        } else {

            // 随机取一个
            basePayConfigurationDO = RandomUtil.randomEle(basePayConfigurationDOList);

        }

        return basePayConfigurationDO;

    }

    /**
     * 获取：默认支付
     */
    public static BasePayConfigurationDO getDefaultBasePayConfigurationDO() {

        BasePayConfigurationDO basePayConfigurationDO =
            basePayConfigurationService.lambdaQuery().eq(BasePayConfigurationDO::getDefaultFlag, true)
                .eq(BasePayConfigurationDO::getEnableFlag, true).one();

        if (basePayConfigurationDO == null) {

            R.errorMsg("操作失败：未配置默认支付方式，请联系管理员");

        }

        return basePayConfigurationDO;

    }

    /**
     * 获取：BasePayConfigurationDO对象
     */
    @Nullable
    public static BasePayConfigurationDO getBasePayConfigurationDO(long basePayConfigurationId) {

        return basePayConfigurationService.lambdaQuery().eq(BasePayConfigurationDO::getId, basePayConfigurationId)
            .one();

    }

    /**
     * 执行：在调用支付前，进行的操作，备注：可以更换支付配置
     */
    public static void execPreDoPayConsumer(PayDTO payDTO) {

        Consumer<PayDTO> preDoPayConsumer = payDTO.getPreDoPayConsumer();

        if (preDoPayConsumer != null) {
            preDoPayConsumer.accept(payDTO); // 执行：检查
        }

    }

}
