package com.kar20240901.be.base.web.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseAuthDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseAuthMapper extends BaseMapper<BaseAuthDO> {

}
