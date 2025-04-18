package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImBlockDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupAddUserDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImBlockGroupPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImBlockGroupPageVO;

public interface BaseImBlockService extends IService<BaseImBlockDO> {

    String addFriend(NotNullId dto);

    String groupAddUser(BaseImBlockGroupAddUserDTO dto);

    String cancelFriend(NotNullId dto);

    String groupCancelUser(BaseImBlockGroupAddUserDTO dto);

    Page<BaseImBlockGroupPageVO> groupPage(BaseImBlockGroupPageDTO dto);

}
