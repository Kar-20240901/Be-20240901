package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserQueryLastContentVO;
import java.util.List;
import java.util.Map;

public interface BaseImSessionRefUserService extends IService<BaseImSessionRefUserDO> {

    void addOrUpdateSessionRefUserForFriend(Long sessionId, Long userId1, Long userId2, boolean addFlag);

    Page<BaseImSessionRefUserPageVO> myPage(BaseImSessionRefUserPageDTO dto);

    List<BaseImSessionRefUserPageVO> scroll(ScrollListDTO dto);

    Map<Long, BaseImSessionRefUserQueryLastContentVO> queryLastContentMap(NotEmptyIdSet dto);

    String hidden(NotNullId dto);

    String updateLastOpenTs(NotNullId dto);

    String updateAvatarAndNickname(NotEmptyIdSet dto);

}
