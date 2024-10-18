package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseParamDO;
import com.kar20240901.be.base.web.model.dto.base.BaseParamInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseParamPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;

public interface BaseParamService extends IService<BaseParamDO> {

    String insertOrUpdate(BaseParamInsertOrUpdateDTO dto);

    Page<BaseParamDO> myPage(BaseParamPageDTO dto);

    BaseParamDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
