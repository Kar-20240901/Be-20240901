package com.kar20240901.be.base.web.service.sms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.sms.BaseSmsConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.sms.BaseSmsConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.sms.BaseSmsConfigurationPageDTO;

public interface BaseSmsConfigurationService extends IService<BaseSmsConfigurationDO> {

    String insertOrUpdate(BaseSmsConfigurationInsertOrUpdateDTO dto);

    Page<BaseSmsConfigurationDO> myPage(BaseSmsConfigurationPageDTO dto);

    BaseSmsConfigurationDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
