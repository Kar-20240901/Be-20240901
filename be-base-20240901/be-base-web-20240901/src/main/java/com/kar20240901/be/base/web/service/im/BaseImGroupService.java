package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveDTO;

public interface BaseImGroupService extends IService<BaseImGroupDO> {

    String removeUser(BaseImGroupRemoveDTO dto);

}
