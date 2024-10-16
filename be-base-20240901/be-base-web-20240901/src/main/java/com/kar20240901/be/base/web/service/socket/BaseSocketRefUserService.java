package com.kar20240901.be.base.web.service.socket;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.socket.BaseSocketRefUserDO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;

public interface BaseSocketRefUserService extends IService<BaseSocketRefUserDO> {

    Page<BaseSocketRefUserDO> myPage(BaseSocketRefUserPageDTO dto);

    String offlineByIdSet(NotEmptyIdSet notEmptyIdSet);

    String changeConsoleFlagByIdSet(NotEmptyIdSet notEmptyIdSet);

}
