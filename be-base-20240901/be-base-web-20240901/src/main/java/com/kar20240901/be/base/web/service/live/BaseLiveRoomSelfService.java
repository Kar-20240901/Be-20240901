package com.kar20240901.be.base.web.service.live;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseLiveRoomDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.live.BaseLiveRoomSelfPageDTO;

public interface BaseLiveRoomSelfService extends IService<BaseLiveRoomDO> {

    String insertOrUpdate(BaseLiveRoomSelfInsertOrUpdateDTO dto);

    Page<BaseLiveRoomDO> myPage(BaseLiveRoomSelfPageDTO dto);

    BaseLiveRoomDO infoById(NotNullId dto);

    String deleteByIdSet(NotEmptyIdSet dto);

}
