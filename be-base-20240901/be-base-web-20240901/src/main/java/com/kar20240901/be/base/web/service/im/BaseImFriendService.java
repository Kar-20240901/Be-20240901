package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;

public interface BaseImFriendService extends IService<BaseImFriendDO> {

    void addOrUpdateFriend(Long sourceUserId, Long targetUserId, Long sessionId, boolean addFlag);

    String remove(NotNullId dto);

}
