package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseApiTokenDO;
import com.kar20240901.be.base.web.model.dto.base.BaseApiTokenInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseApiTokenPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;

public interface BaseApiTokenService extends IService<BaseApiTokenDO> {

    String insertOrUpdate(BaseApiTokenInsertOrUpdateDTO dto);

    Page<BaseApiTokenDO> myPage(BaseApiTokenPageDTO dto);

    BaseApiTokenDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
