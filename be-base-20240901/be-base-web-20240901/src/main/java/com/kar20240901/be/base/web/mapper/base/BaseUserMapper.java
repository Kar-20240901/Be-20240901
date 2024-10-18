package com.kar20240901.be.base.web.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.base.TempUserDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserPageDTO;
import com.kar20240901.be.base.web.model.vo.base.BaseUserPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseUserMapper extends BaseMapper<TempUserDO> {

    Page<BaseUserPageVO> myPage(@Param("page") Page<BaseUserPageVO> page, @Param("dto") BaseUserPageDTO dto);

}
