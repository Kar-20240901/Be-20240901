package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSendDTO;

public interface BaseImApplyFriendService extends IService<BaseImApplyFriendDO> {

    String send(BaseImApplyFriendSendDTO dto);

}
