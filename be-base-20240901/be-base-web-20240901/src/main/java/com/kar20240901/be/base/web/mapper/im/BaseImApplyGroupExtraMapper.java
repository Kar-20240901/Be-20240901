package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupExtraDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImApplyGroupExtraMapper extends BaseMapper<BaseImApplyGroupExtraDO> {

    void insertOrUpdateHiddenFlag(@Param("applyGroupId") Long applyGroupId, @Param("userId") Long userId,
        @Param("hiddenFlag") boolean hiddenFlag);

}
