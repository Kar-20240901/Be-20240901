package com.kar20240901.be.base.web.service.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomUserSelfPageDTO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserSelfInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.live.BaseLiveRoomUserSelfPageVO;

public interface BaseLiveRoomUserSelfService extends IService<BaseLiveRoomUserDO> {

    Page<BaseLiveRoomUserSelfPageVO> myPage(BaseLiveRoomUserSelfPageDTO dto);

    BaseLiveRoomUserSelfInfoByIdVO infoById(NotNullId dto);

    String deleteByIdSet(NotEmptyIdSet dto);

}
