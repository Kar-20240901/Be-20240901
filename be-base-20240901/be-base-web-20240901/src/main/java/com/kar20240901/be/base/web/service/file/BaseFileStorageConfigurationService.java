package com.kar20240901.be.base.web.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.file.BaseFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileStorageConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.file.BaseFileStorageConfigurationPageDTO;

public interface BaseFileStorageConfigurationService extends IService<BaseFileStorageConfigurationDO> {

    String insertOrUpdate(BaseFileStorageConfigurationInsertOrUpdateDTO dto);

    Page<BaseFileStorageConfigurationDO> myPage(BaseFileStorageConfigurationPageDTO dto);

    BaseFileStorageConfigurationDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
