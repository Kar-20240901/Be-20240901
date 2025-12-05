package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendPageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendRejectDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSearchApplyFriendDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSendDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImApplyFriendSearchApplyFriendVO;

public interface BaseImApplyFriendService extends IService<BaseImApplyFriendDO> {

    Page<BaseImApplyFriendSearchApplyFriendVO> searchApplyFriend(BaseImApplyFriendSearchApplyFriendDTO dto);

    String send(BaseImApplyFriendSendDTO dto);

    Page<BaseImApplyFriendPageVO> myPage(BaseImApplyFriendPageDTO dto);

    String agree(NotEmptyIdSet dto);

    String reject(BaseImApplyFriendRejectDTO dto);

    String hidden(NotEmptyIdSet dto);

    String cancel(NotEmptyIdSet dto);

}
