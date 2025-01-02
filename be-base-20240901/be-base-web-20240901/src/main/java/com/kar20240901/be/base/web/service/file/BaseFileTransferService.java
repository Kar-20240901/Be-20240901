package com.kar20240901.be.base.web.service.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.file.BaseFileTransferPageDTO;

public interface BaseFileTransferService extends IService<BaseFileTransferDO> {

    Page<BaseFileTransferDO> myPage(BaseFileTransferPageDTO dto);

    BaseFileTransferDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
