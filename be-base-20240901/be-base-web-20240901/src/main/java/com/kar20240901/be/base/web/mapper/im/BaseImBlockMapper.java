package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import java.util.Date;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImBlockMapper extends BaseMapper<BaseImBlockDO> {

    void insertOrUpdateForCreate(@Param("userIdSet") Set<Long> userIdSet, @Param("createId") Long createId,
        @Param("createTime") Date createTime, @Param("sourceId") Long sourceId, @Param("sourceType") int sourceType);

}
