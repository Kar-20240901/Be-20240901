package com.kar20240901.be.base.web.util.im;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.im.BaseImBlockMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaseImBlockUtil {

    private static BaseImBlockMapper baseImBlockMapper;

    @Resource
    public void setBaseImBlockMapper(BaseImBlockMapper baseImBlockMapper) {
        BaseImBlockUtil.baseImBlockMapper = baseImBlockMapper;
    }

    /**
     * 获取：当前用户 id和 userIdList，获取拉黑的 userIdSet
     */
    public static Set<Long> getBlockUserIdSet(Long currentUserId, List<Long> userIdList) {

        List<BaseImBlockDO> baseImBlockDOList =
            ChainWrappers.lambdaQueryChain(baseImBlockMapper).eq(BaseImBlockDO::getSourceId, currentUserId)
                .in(BaseImBlockDO::getUserId, userIdList).select(BaseImBlockDO::getUserId).list();

        if (CollUtil.isEmpty(baseImBlockDOList)) {
            return new HashSet<>();
        }

        return baseImBlockDOList.stream().map(BaseImBlockDO::getUserId).collect(Collectors.toSet());

    }

}
