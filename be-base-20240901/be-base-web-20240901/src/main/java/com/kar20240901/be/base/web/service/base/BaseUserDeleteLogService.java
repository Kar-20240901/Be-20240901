package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseUserDeleteLogDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserDeleteLogPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;

public interface BaseUserDeleteLogService extends IService<BaseUserDeleteLogDO> {

    Page<BaseUserDeleteLogDO> myPage(BaseUserDeleteLogPageDTO dto);

    BaseUserDeleteLogDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
