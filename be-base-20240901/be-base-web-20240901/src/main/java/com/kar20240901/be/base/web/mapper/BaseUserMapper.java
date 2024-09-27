package com.kar20240901.be.base.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.BaseUserPageDTO;
import com.kar20240901.be.base.web.model.vo.BaseUserPageVO;
import com.kar20240901.be.base.web.model.domain.BaseUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseUserMapper extends BaseMapper<BaseUserDO> {

    Page<BaseUserPageVO> myPage(@Param("page") Page<BaseUserPageVO> page, @Param("dto") BaseUserPageDTO dto);

}
