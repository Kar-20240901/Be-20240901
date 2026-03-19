package com.kar20240901.be.base.web.mapper.im;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.im.BaseImSearchHistoryDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchHistoryPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseImSearchHistoryMapper extends BaseMapper<BaseImSearchHistoryDO> {

    Page<BaseImSearchHistoryVO> searchHistoryPage(@Param("page") Page<Object> page,
        @Param("dto") BaseImSearchHistoryPageDTO dto, @Param("currentUserId") Long currentUserId);

}
