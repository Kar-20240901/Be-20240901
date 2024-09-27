package com.kar20240901.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.dto.BaseParamInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.BaseParamPageDTO;
import com.kar20240901.be.base.web.model.domain.BaseParamDO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.NotNullId;

public interface BaseParamService extends IService<BaseParamDO> {

    String insertOrUpdate(BaseParamInsertOrUpdateDTO dto);

    Page<BaseParamDO> myPage(BaseParamPageDTO dto);

    BaseParamDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
