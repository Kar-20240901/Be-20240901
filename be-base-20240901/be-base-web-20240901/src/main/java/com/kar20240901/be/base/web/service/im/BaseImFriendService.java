package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;

public interface BaseImFriendService extends IService<BaseImFriendDO> {

    void addFriend(Long sourceUserId, Long targetUserId, Long sessionId);

    String remove(NotNullId dto);

}
