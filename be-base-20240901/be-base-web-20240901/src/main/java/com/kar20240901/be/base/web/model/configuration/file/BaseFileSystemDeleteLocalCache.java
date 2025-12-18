package com.kar20240901.be.base.web.model.configuration.file;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.configuration.base.IBaseDeleteLocalCache;
import com.kar20240901.be.base.web.model.enums.base.BaseDeleteLocalCacheTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseDeleteLocalCacheType;
import com.kar20240901.be.base.web.util.file.BaseFileUtil;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class BaseFileSystemDeleteLocalCache implements IBaseDeleteLocalCache {

    @Override
    public IBaseDeleteLocalCacheType getType() {
        return BaseDeleteLocalCacheTypeEnum.DELETE_FILE_SYSTEM_CLIENT_CACHE;
    }

    /**
     * 处理
     */
    @Override
    public void handle(BaseDeleteLocalCacheBO baseDeleteLocalCacheBO) {

        Set<Long> baseFileStorageConfigurationIdSet = baseDeleteLocalCacheBO.getRefIdSet();

        if (CollUtil.isEmpty(baseFileStorageConfigurationIdSet)) {
            return;
        }

        for (Long item : baseFileStorageConfigurationIdSet) {

            BaseFileUtil.clearByIdBaseFileStorageClientMap(item);

        }

    }

}
