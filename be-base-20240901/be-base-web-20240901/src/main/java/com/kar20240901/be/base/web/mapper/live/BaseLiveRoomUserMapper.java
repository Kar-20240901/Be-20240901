package com.kar20240901.be.base.web.mapper.live;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserPageDTO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserPageVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseLiveRoomUserMapper extends BaseMapper<BaseLiveRoomUserDO> {

    List<Long> checkRoomUser();

    Page<BaseLiveRoomUserPageVO> myPage(@Param("page") Page<BaseLiveRoomUserPageVO> page,
        @Param("dto") BaseLiveRoomUserPageDTO dto);

    BaseLiveRoomUserInfoByIdVO infoById(@Param("id") Long id);

}
