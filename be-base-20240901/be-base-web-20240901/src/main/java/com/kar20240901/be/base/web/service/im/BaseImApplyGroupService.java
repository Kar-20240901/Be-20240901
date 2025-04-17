package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;

public interface BaseImApplyGroupService extends IService<BaseImApplyGroupDO> {

    String insertOrUpdate(BaseImApplyGroupInsertOrUpdateDTO dto);

    String send(BaseImApplyGroupSendDTO dto);

}
