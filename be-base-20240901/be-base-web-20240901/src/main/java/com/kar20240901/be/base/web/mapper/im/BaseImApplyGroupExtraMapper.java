package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupExtraDO;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImApplyGroupExtraMapper extends BaseMapper<BaseImApplyGroupExtraDO> {

    void insertOrUpdateHiddenFlag(@Param("applyGroupIdSet") Set<Long> applyGroupIdSet,
        @Param("userOrGroupId") Long userOrGroupId, @Param("hiddenFlag") boolean hiddenFlag, @Param("type") int type);

}
