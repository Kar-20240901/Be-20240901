package com.kar20240901.be.base.web.model.configuration.file;

import cn.hutool.core.collection.CollUtil;
import com.kar20240901.be.base.web.model.bo.base.BaseDeleteLocalCacheBO;
import com.kar20240901.be.base.web.model.configuration.base.IBaseDeleteLocalCache;
import com.kar20240901.be.base.web.model.enums.base.BaseDeleteLocalCacheTypeEnum;
import com.kar20240901.be.base.web.model.interfaces.base.IBaseDeleteLocalCacheType;
import com.kar20240901.be.base.web.util.file.BaseFileUtil;
import com.kar20240901.be.base.web.util.kafka.TempKafkaUtil;
import java.util.ArrayList;
import java.util.List;
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

        List<String> patternList = new ArrayList<>();

        for (Long item : baseFileStorageConfigurationIdSet) {

            BaseFileUtil.clearByIdBaseFileStorageClientMap(item);

            patternList.add(BaseFileUtil.REDIS_PRE_KEY + item + ":*");

        }

        TempKafkaUtil.sendDeleteCacheByPatternTopic(patternList);

    }

}
