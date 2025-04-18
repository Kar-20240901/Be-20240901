package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendExtraDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImApplyFriendExtraMapper extends BaseMapper<BaseImApplyFriendExtraDO> {

    void insertOrUpdateHiddenFlag(@Param("applyFriendId") Long applyFriendId, @Param("userId") Long userId,
        @Param("hiddenFlag") boolean hiddenFlag);

}
