package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupAddUserDTO;

public interface BaseImBlockService extends IService<BaseImBlockDO> {

    String addFriend(NotEmptyIdSet dto);

    String groupAddUser(BaseImBlockGroupAddUserDTO dto);

    String cancelFriend(NotEmptyIdSet dto);

    String groupCancelUser(BaseImBlockGroupAddUserDTO dto);

}
