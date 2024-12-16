package com.kar20240901.be.base.web.service.pay;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.enums.pay.BasePayTradeStatusEnum;

public interface BasePayService extends IService<BasePayDO> {

    BasePayTradeStatusEnum payTradeStatusById(NotNullId notNullId);

    BasePayTradeStatusEnum payTradeStatusByIdOther(NotNullId notNullId);

}
