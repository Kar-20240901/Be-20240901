package com.kar20240901.be.base.web.mapper.live;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseLiveRoomUserMapper extends BaseMapper<BaseLiveRoomUserDO> {

    List<Long> checkRoomUser();

}
