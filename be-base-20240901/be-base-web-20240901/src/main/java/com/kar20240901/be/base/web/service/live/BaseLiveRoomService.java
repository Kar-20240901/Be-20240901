package com.kar20240901.be.base.web.service.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomPageDTO;

public interface BaseLiveRoomService extends IService<BaseLiveRoomDO> {

    String insertOrUpdate(BaseLiveRoomInsertOrUpdateDTO dto);

    Page<BaseLiveRoomDO> myPage(BaseLiveRoomPageDTO dto);

    BaseLiveRoomDO infoById(NotNullId dto);

    String deleteByIdSet(NotEmptyIdSet dto);

    String refreshCode(NotNullId dto);

}
