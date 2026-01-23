package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImFriendDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImFriendPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImFriendPageVO;
import java.util.List;

public interface BaseImFriendService extends IService<BaseImFriendDO> {

    void addOrUpdateFriend(Long sourceUserId, Long targetUserId, Long sessionId, boolean addFlag);

    Page<BaseImFriendPageVO> myPage(BaseImFriendPageDTO dto);

    List<BaseImFriendPageVO> scroll(ScrollListDTO dto);

    String removeFriend(NotEmptyIdSet dto);

}
