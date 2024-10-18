package com.kar20240901.be.base.web.service.socket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.socket.BaseSocketPageDTO;

public interface BaseSocketService extends IService<BaseSocketDO> {

    Page<BaseSocketDO> myPage(BaseSocketPageDTO dto);

    String disableByIdSet(NotEmptyIdSet notEmptyIdSet);

    String enableByIdSet(NotEmptyIdSet notEmptyIdSet);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
