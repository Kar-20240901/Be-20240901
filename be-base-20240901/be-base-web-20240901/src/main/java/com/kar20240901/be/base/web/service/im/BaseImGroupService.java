package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupChangeBelongIdDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveUserDTO;

public interface BaseImGroupService extends IService<BaseImGroupDO> {

    String insertOrUpdate(BaseImGroupInsertOrUpdateDTO dto);

    String removeUser(BaseImGroupRemoveUserDTO dto);

    String changeBelongId(BaseImGroupChangeBelongIdDTO dto);

}
