package com.kar20240901.be.base.web.configuration.wallet;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.pay.BasePayMapper;
import com.kar20240901.be.base.web.model.configuration.pay.IBasePayRefHandler;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayRefStatusEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayRefTypeEnum;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.model.enums.wallet.BaseUserWalletLogTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefType;
import com.kar20240901.be.base.web.service.wallet.BaseUserWalletService;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import com.kar20240901.be.base.web.util.base.TransactionUtil;
import com.kar20240901.be.base.web.util.pay.PayHelper;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 购买算力的订单支付回调处理
 */
@Component
public class BaseWalletUserBasePayRefHandlerConfiguration implements IBasePayRefHandler {

    @Resource
    BaseUserWalletService baseUserWalletService;

    @Resource
    BasePayMapper basePayMapper;

    /**
     * 关联的类型
     */
    @Override
    public IBasePayRefType getBasePayRefType() {
        return BasePayRefTypeEnum.WALLET_RECHARGE_USER;
    }

    /**
     * 执行处理
     */
    @Override
    public void handle(BasePayDO basePayDO) {

        if (!BasePayTradeStatusEnum.TRADE_SUCCESS.equals(basePayDO.getStatus())) {
            return;
        }

        if (basePayDO.getRefType() != getBasePayRefType().getCode()) {
            return;
        }

        // 获取：订单关联的 id
        Long refId = basePayDO.getRefId();

        if (refId == null) {
            return;
        }

        if (BasePayRefStatusEnum.FINISHED.getCode() == basePayDO.getRefStatus()) {
            return;
        }

        // 异步执行
        MyThreadUtil.execute(() -> {

            RedissonUtil.doLock(BaseRedisKeyEnum.PRE_PAY.name() + ":" + basePayDO.getId(), () -> {

                // 再查询一次：是否已经处理过该支付
                boolean exists = ChainWrappers.lambdaQueryChain(basePayMapper).eq(BasePayDO::getId, basePayDO.getId())
                    .eq(BasePayDO::getRefStatus, BasePayRefStatusEnum.WAIT_PAY.getCode()).exists();

                if (!exists) { // 如果：已经处理过了，则不再处理
                    return;
                }

                String refData = basePayDO.getRefData();

                Long userId = basePayDO.getUserId();

                Date date = new Date();

                TransactionUtil.exec(() -> {

                    // 增加用户的：可提现余额
                    baseUserWalletService.doAddWithdrawableMoney(userId, date, CollUtil.newHashSet(refId),
                        basePayDO.getOriginalPrice(), BaseUserWalletLogTypeEnum.ADD_PAY, false, false, refId, refData,
                        true, null);

                    basePayDO.setRefStatus(BasePayRefStatusEnum.FINISHED.getCode());

                    basePayMapper.updateById(basePayDO); // 更新：支付的关联状态

                });

                // 关闭：前端支付弹窗
                PayHelper.sendBasePayCloseModalTopic(basePayDO);

            });

        });

    }

}
