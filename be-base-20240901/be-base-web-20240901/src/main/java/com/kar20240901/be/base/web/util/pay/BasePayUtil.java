package com.kar20240901.be.base.web.util.pay;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.model.bo.pay.BasePayReturnBO;
import com.kar20240901.be.base.web.model.bo.pay.BasePayTradeNotifyBO;
import com.kar20240901.be.base.web.model.configuration.pay.IBasePay;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.dto.pay.PayDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayRefStatusEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayRefTypeEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.pay.BasePayConfigurationService;
import com.kar20240901.be.base.web.service.pay.BasePayService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import com.kar20240901.be.base.web.util.base.MyEntityUtil;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.base.TransactionUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = LogTopicConstant.PAY)
public class BasePayUtil {

    private static final Map<Integer, IBasePay> BASE_PAY_MAP = MapUtil.newHashMap();

    private static BasePayService basePayService;

    private static BasePayConfigurationService basePayConfigurationService;

    public BasePayUtil(@Autowired(required = false) @Nullable List<IBasePay> iBasePayList,
        BasePayService basePayService, BasePayConfigurationService basePayConfigurationService) {

        BasePayUtil.basePayService = basePayService;

        BasePayUtil.basePayConfigurationService = basePayConfigurationService;

        if (CollUtil.isNotEmpty(iBasePayList)) {

            for (IBasePay item : iBasePayList) {

                BASE_PAY_MAP.put(item.getBasePayType().getCode(), item);

            }

        }

    }

    private static CopyOnWriteArrayList<BasePayDO> BASE_PAY_DO_LIST = new CopyOnWriteArrayList<>();

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        CopyOnWriteArrayList<BasePayDO> tempBasePayDoList;

        synchronized (BASE_PAY_DO_LIST) {

            if (CollUtil.isEmpty(BASE_PAY_DO_LIST)) {
                return;
            }

            tempBasePayDoList = BASE_PAY_DO_LIST;
            BASE_PAY_DO_LIST = new CopyOnWriteArrayList<>();

        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            // 批量保存数据
            basePayService.updateBatchById(tempBasePayDoList);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 支付
     *
     * @param consumer 注意：BasePayDO对象，只建议修改：refType 和 refId这两个属性，其他属性不建议修改
     */
    public static BasePayDO pay(PayDTO dto, @Nullable Consumer<BasePayDO> consumer) {

        if (dto.getBasePayConfigurationDO() != null) {
            dto.setPayType(dto.getBasePayConfigurationDO().getType()); // 保证一致性
        }

        checkPayDTO(dto); // 检查：dto对象

        handleBasePayConfigurationDO(dto); // 处理：支付方式

        BasePayHelper.execPreDoPayConsumer(dto); // 执行：在调用支付前，进行的操作，备注：可以更换支付配置

        IBasePay iBasePay = BASE_PAY_MAP.get(dto.getPayType());

        if (iBasePay == null) {
            R.errorMsg("操作失败：支付方式未找到：{}", dto.getPayType());
        }

        if (StrUtil.isBlank(dto.getOpenId())) {
            setOpenId(dto); // 设置：openId
        }

        Long basePayId = IdGeneratorUtil.nextId();

        dto.setOutTradeNo(basePayId.toString()); // 设置：支付的订单号

        // 调用：第三方支付
        BasePayReturnBO basePayReturnBO = iBasePay.pay(dto);

        // 获取：BasePayDO对象
        BasePayDO basePayDO = getBasePayDO(dto, iBasePay, basePayId, basePayReturnBO);

        TransactionUtil.exec(() -> {

            if (consumer != null) {

                consumer.accept(basePayDO);

            }

            basePayService.save(basePayDO);

        });

        return basePayDO;

    }

    /**
     * 处理：支付方式
     */
    public static void handleBasePayConfigurationDO(PayDTO dto) {

        BasePayConfigurationDO basePayConfigurationDO = dto.getBasePayConfigurationDO();

        if (basePayConfigurationDO == null) {

            if (dto.getPayType() == null) { // 如果是：默认支付

                basePayConfigurationDO = BasePayHelper.getDefaultBasePayConfigurationDO();

            } else {

                basePayConfigurationDO = BasePayHelper.getBasePayConfigurationDO(dto.getPayType());

            }

        }

        dto.setPayType(basePayConfigurationDO.getType());
        dto.setBasePayConfigurationDO(basePayConfigurationDO);

    }

    /**
     * 设置：openId
     */
    private static void setOpenId(PayDTO dto) {

        String openId;

        if (dto.getPayType() >= BasePayTypeEnum.WX_NATIVE.getCode()
            && dto.getPayType() < BasePayTypeEnum.UNION.getCode()) {

            openId = MyUserUtil.getCurrentUserWxOpenIdDefault();

        } else {

            openId = "";

        }

        dto.setOpenId(openId);

    }

    /**
     * 检查：dto对象
     */
    private static void checkPayDTO(PayDTO dto) {

        Assert.notNull(dto.getUserId());

        Assert.notNull(dto.getTotalAmount());
        Assert.notBlank(dto.getSubject());

        int compare = DateUtil.compare(dto.getExpireTime(), new Date());

        if (compare <= 0) {
            R.errorMsg("操作失败：支付时间已过期");
        }

    }

    /**
     * 获取：BasePayDO对象
     */
    @NotNull
    private static BasePayDO getBasePayDO(PayDTO dto, IBasePay iBasePay, Long payId, BasePayReturnBO basePayReturnBO) {

        BasePayDO basePayDO = new BasePayDO();

        basePayDO.setId(payId);

        basePayDO.setPayType(iBasePay.getBasePayType().getCode());

        basePayDO.setUserId(dto.getUserId());

        basePayDO.setSubject(dto.getSubject());
        basePayDO.setBody(MyEntityUtil.getNotNullStr(dto.getBody()));
        basePayDO.setOriginalPrice(dto.getTotalAmount());
        basePayDO.setPayPrice(BigDecimal.ZERO);
        basePayDO.setPayCurrency("");

        basePayDO.setExpireTime(dto.getExpireTime());

        basePayDO.setBasePayConfigurationId(dto.getBasePayConfigurationDO().getId());

        basePayDO.setOpenId(MyEntityUtil.getNotNullAndTrimStr(dto.getOpenId()));

        basePayDO.setPayAppId(MyEntityUtil.getNotNullStr(basePayReturnBO.getPayAppId()));

        basePayDO.setPayReturnValue(MyEntityUtil.getNotNullStr(basePayReturnBO.getPayReturnValue()));

        basePayDO.setStatus(BasePayTradeStatusEnum.WAIT_BUYER_PAY);

        basePayDO.setTradeNo("");

        basePayDO.setRefType(BasePayRefTypeEnum.NONE.getCode());
        basePayDO.setRefId(TempConstant.NEGATIVE_ONE);
        basePayDO.setRefData("");
        basePayDO.setRefStatus(BasePayRefStatusEnum.NONE.getCode());

        basePayDO.setPackageName(MyEntityUtil.getNotNullAndTrimStr(dto.getPackageName()));
        basePayDO.setProductId(MyEntityUtil.getNotNullAndTrimStr(dto.getProductId()));
        basePayDO.setToken(MyEntityUtil.getNotNullAndTrimStr(dto.getToken()));

        basePayDO.setEnableFlag(true);
        basePayDO.setRemark(MyEntityUtil.getNotNullStr(dto.getRemark()));

        return basePayDO;

    }

    /**
     * 查询订单状态
     *
     * @param outTradeNo 本系统的支付主键 id，必填
     */
    public static BasePayTradeStatusEnum query(String outTradeNo) {

        BasePayDO basePayDO = basePayService.lambdaQuery().eq(BasePayDO::getId, outTradeNo).one();

        if (basePayDO == null) {
            R.error("操作失败：支付不存在", outTradeNo);
        }

        IBasePay iBasePay = BASE_PAY_MAP.get(basePayDO.getPayType());

        if (iBasePay == null) {
            R.error("操作失败：支付方式未找到", basePayDO.getPayType());
        }

        Long basePayConfigurationId = basePayDO.getBasePayConfigurationId();

        BasePayConfigurationDO basePayConfigurationDO =
            basePayConfigurationService.lambdaQuery().eq(TempEntity::getId, basePayConfigurationId).one();

        if (basePayConfigurationDO == null) {
            R.error("操作失败：支付配置未找到", basePayConfigurationId);
        }

        // 执行查询
        return iBasePay.query(outTradeNo, basePayConfigurationDO);

    }

    /**
     * 处理：订单回调
     */
    public static boolean handleTradeNotify(@Nullable BasePayTradeNotifyBO basePayTradeNotifyBO,
        @Nullable Consumer<BasePayDO> consumer) {

        if (basePayTradeNotifyBO == null) {
            return false;
        }

        // 获取：支付状态
        BasePayTradeStatusEnum basePayTradeStatusEnum =
            BasePayTradeStatusEnum.getByStatus(basePayTradeNotifyBO.getTradeStatus());

        if (BasePayTradeStatusEnum.NOT_EXIST.equals(basePayTradeStatusEnum)) {
            return false;
        }

        return RedissonUtil.doLock(BaseRedisKeyEnum.PRE_PAY.name() + ":" + basePayTradeNotifyBO.getOutTradeNo(), () -> {

            // 查询：支付状态不同的数据
            BasePayDO basePayDO =
                basePayService.lambdaQuery().eq(BasePayDO::getId, basePayTradeNotifyBO.getOutTradeNo())
                    .ne(BasePayDO::getStatus, basePayTradeStatusEnum).one();

            if (basePayDO == null) {
                return false;
            }

            basePayDO.setPayPrice(new BigDecimal(basePayTradeNotifyBO.getTotalAmount()));
            basePayDO.setStatus(basePayTradeStatusEnum);
            basePayDO.setTradeNo(basePayTradeNotifyBO.getTradeNo());
            basePayDO.setPayCurrency(MyEntityUtil.getNotNullStr(basePayTradeNotifyBO.getPayCurrency()));

            if (consumer != null) {

                consumer.accept(basePayDO); // 进行额外的处理

            }

            BASE_PAY_DO_LIST.add(basePayDO);

            if (BasePayRefTypeEnum.NONE.getCode() != basePayDO.getRefType()) {

                // 支付成功，处理业务
                TempKafkaUtil.sendPayStatusChangeTopic(basePayDO);

            }

            return true;

        });

    }

}
