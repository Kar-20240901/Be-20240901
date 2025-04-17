package com.kar20240901.be.base.web.service.thirdapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.thirdapp.BaseThirdAppDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppPageDTO;

public interface BaseThirdAppService extends IService<BaseThirdAppDO> {

    String insertOrUpdate(BaseThirdAppInsertOrUpdateDTO dto);

    Page<BaseThirdAppDO> myPage(BaseThirdAppPageDTO dto);

    BaseThirdAppDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String getNameById(NotNullId notNullId);

}
