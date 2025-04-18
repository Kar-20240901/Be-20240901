package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;

public interface BaseImFriendService extends IService<BaseImFriendDO> {

    Long addFriend(Long sourceUserId, Long targetUserId);

}
