package com.kar20240901.be.base.web.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.file.SysFileStorageConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.SysFileStorageConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.file.SysFileStorageConfigurationPageDTO;

public interface SysFileStorageConfigurationService extends IService<SysFileStorageConfigurationDO> {

    String insertOrUpdate(SysFileStorageConfigurationInsertOrUpdateDTO dto);

    Page<SysFileStorageConfigurationDO> myPage(SysFileStorageConfigurationPageDTO dto);

    SysFileStorageConfigurationDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
