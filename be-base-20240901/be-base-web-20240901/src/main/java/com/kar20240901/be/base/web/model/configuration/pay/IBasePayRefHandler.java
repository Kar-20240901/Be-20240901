package com.kar20240901.be.base.web.model.configuration.pay;

import com.kar20240901.be.base.web.model.domain.pay.BasePayDO;
import com.kar20240901.be.base.web.model.interfaces.pay.IBasePayRefType;

public interface IBasePayRefHandler {

    /**
     * 关联的类型
     */
    IBasePayRefType getBasePayRefType();

    /**
     * 执行处理，注意：不建议改变 basePayDO对象里面的属性值
     */
    void handle(BasePayDO basePayDO);

}
