package com.kar20240901.be.base.web.service.pay.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.pay.BasePayMapper;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.service.pay.BasePayService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.pay.PayUtil;
import org.springframework.stereotype.Service;

@Service
public class BasePayServiceImpl extends ServiceImpl<BasePayMapper, BasePayDO> implements BasePayService {

    /**
     * 通过主键id，查看支付状态-本平台
     */
    @Override
    public BasePayTradeStatusEnum payTradeStatusById(NotNullId notNullId) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        BasePayDO basePayDO =
            lambdaQuery().eq(BasePayDO::getId, notNullId.getId()).eq(BasePayDO::getUserId, currentUserId)
                .select(BasePayDO::getStatus).one();

        if (basePayDO == null) {
            return null;
        }

        return basePayDO.getStatus();

    }

    /**
     * 通过主键id，查看支付状态-第三方支付平台
     */
    @Override
    public BasePayTradeStatusEnum payTradeStatusByIdOther(NotNullId notNullId) {

        // 查询：第三方的支付状态
        return PayUtil.query(notNullId.getId().toString());

    }

}
