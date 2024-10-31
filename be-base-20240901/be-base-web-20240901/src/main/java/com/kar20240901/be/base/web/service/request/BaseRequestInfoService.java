package com.kar20240901.be.base.web.service.request;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;

public interface BaseRequestInfoService extends IService<BaseRequestInfoDO> {

    BaseRequestInfoDO infoById(NotNullId notNullId);

}
