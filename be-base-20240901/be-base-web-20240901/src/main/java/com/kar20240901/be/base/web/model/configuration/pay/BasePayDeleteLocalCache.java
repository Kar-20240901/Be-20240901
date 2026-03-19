package com.kar20240901.be.base.web.model.configuration.pay;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.configuration.base.IBaseDeleteLocalCache;
import com.kar20240901.be.base.web.model.enums.base.BaseDeleteLocalCacheTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseDeleteLocalCacheType;
import com.kar20240901.be.base.web.util.pay.BasePayHelper;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class BasePayDeleteLocalCache implements IBaseDeleteLocalCache {

    @Override
    public IBaseDeleteLocalCacheType getType() {
        return BaseDeleteLocalCacheTypeEnum.DELETE_PAY_CLIENT_CACHE;
    }

    /**
     * 处理
     */
    @Override
    public void handle(BaseDeleteLocalCacheBO baseDeleteLocalCacheBO) {

        Set<Long> basePayConfigurationIdSet = baseDeleteLocalCacheBO.getRefIdSet();

        if (CollUtil.isEmpty(basePayConfigurationIdSet)) {
            return;
        }

        for (Long item : basePayConfigurationIdSet) {

            BasePayHelper.clearByIdBasePayClientMap(item);

        }

    }

}
