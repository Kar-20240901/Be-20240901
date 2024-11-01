package com.kar20240901.be.base.web.service.otherapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.otherapp.BaseOtherAppPageDTO;

public interface BaseOtherAppService extends IService<BaseOtherAppDO> {

    String insertOrUpdate(BaseOtherAppInsertOrUpdateDTO dto);

    Page<BaseOtherAppDO> myPage(BaseOtherAppPageDTO dto);

    BaseOtherAppDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String getNameById(NotNullId notNullId);

}
