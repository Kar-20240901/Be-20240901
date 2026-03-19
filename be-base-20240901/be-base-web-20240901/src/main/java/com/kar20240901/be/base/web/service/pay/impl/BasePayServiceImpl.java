package com.kar20240901.be.base.web.service.pay.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.pay.BasePayMapper;
import com.kar20240901.be.base.web.model.domain.base.TempEntity;
import com.kar20240901.be.base.web.model.domain.base.TempEntityNoIdSuper;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.pay.BasePayPageDTO;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;
import com.kar20240901.be.base.web.service.pay.BasePayService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.pay.BasePayUtil;
import org.springframework.stereotype.Service;

@Service
public class BasePayServiceImpl extends ServiceImpl<BasePayMapper, BasePayDO> implements BasePayService {

    /**
     * 分页排序查询
     */
    @Override
    public Page<BasePayDO> myPage(BasePayPageDTO dto) {

        return lambdaQuery().eq(dto.getId() != null, BasePayDO::getId, dto.getId())
            .eq(dto.getPayType() != null, BasePayDO::getPayType, dto.getPayType())
            .eq(dto.getBasePayConfigurationId() != null, BasePayDO::getBasePayConfigurationId,
                dto.getBasePayConfigurationId()) //
            .eq(dto.getStatus() != null, BasePayDO::getStatus, dto.getStatus())
            .eq(dto.getUserId() != null, BasePayDO::getUserId, dto.getUserId())
            .eq(dto.getRefStatus() != null, BasePayDO::getRefStatus, dto.getRefStatus())
            .select(TempEntity::getId, TempEntityNoIdSuper::getCreateId, TempEntityNoIdSuper::getCreateTime,
                TempEntityNoIdSuper::getUpdateId, TempEntityNoIdSuper::getUpdateTime, BasePayDO::getPayType,
                BasePayDO::getOriginalPrice, BasePayDO::getPayPrice, BasePayDO::getStatus, BasePayDO::getRefStatus)
            .page(dto.updateTimeDescDefaultOrderPage());

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BasePayDO infoById(NotNullId notNullId) {

        return lambdaQuery().eq(TempEntity::getId, notNullId.getId()).one();

    }

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
    public BasePayTradeStatusEnum payTradeStatusByIdThird(NotNullId notNullId) {

        // 查询：第三方的支付状态
        return BasePayUtil.query(notNullId.getId().toString());

    }

}
