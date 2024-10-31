package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseEmailConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseEmailConfigurationInsertOrUpdateDTO;

public interface BaseEmailConfigurationService extends IService<BaseEmailConfigurationDO> {

    String insertOrUpdate(BaseEmailConfigurationInsertOrUpdateDTO dto);

    BaseEmailConfigurationDO infoById();

}
