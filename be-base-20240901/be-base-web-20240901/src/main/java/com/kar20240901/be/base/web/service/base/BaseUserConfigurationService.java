package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserConfigurationInsertOrUpdateDTO;

public interface BaseUserConfigurationService extends IService<BaseUserConfigurationDO> {

    String insertOrUpdate(BaseUserConfigurationInsertOrUpdateDTO dto);

    BaseUserConfigurationDO infoById();

}
