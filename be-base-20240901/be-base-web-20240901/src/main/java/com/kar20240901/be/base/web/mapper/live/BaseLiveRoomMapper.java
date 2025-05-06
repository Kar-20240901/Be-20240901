package com.kar20240901.be.base.web.mapper.live;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomPageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseLiveRoomMapper extends BaseMapper<BaseLiveRoomDO> {

    Page<BaseLiveRoomDO> myPage(@Param("page") Page<BaseLiveRoomDO> page, @Param("dto") BaseLiveRoomPageDTO dto);

}
