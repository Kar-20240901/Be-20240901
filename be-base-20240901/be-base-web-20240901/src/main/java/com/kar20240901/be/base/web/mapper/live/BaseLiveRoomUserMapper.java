package com.kar20240901.be.base.web.mapper.live;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserSelfInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserSelfPageVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseLiveRoomUserMapper extends BaseMapper<BaseLiveRoomUserDO> {

    List<Long> checkRoomUser();

    Page<BaseLiveRoomUserSelfPageVO> myPage(@Param("page") Page<BaseLiveRoomUserSelfPageVO> page,
        @Param("dto") BaseLiveRoomUserSelfPageDTO dto);

    BaseLiveRoomUserSelfInfoByIdVO infoById(@Param("id") Long id);

}
