package com.kar20240901.be.base.web.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseAuthDO;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.Nullable;

@Mapper
public interface BaseAuthMapper extends BaseMapper<BaseAuthDO> {

    Set<String> getAuthSetByUserId(@Nullable @Param("userId") Long userId);

}
