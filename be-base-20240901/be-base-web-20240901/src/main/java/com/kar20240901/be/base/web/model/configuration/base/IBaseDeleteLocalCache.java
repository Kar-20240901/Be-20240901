package com.kar20240901.be.base.web.model.configuration.base;

import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseDeleteLocalCacheType;

public interface IBaseDeleteLocalCache {

    IBaseDeleteLocalCacheType getType();

    /**
     * 执行处理
     */
    void handle(BaseDeleteLocalCacheBO baseDeleteLocalCacheBO);

}
