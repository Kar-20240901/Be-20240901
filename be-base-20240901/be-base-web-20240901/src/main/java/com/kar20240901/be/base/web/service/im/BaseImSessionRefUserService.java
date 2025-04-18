package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImSessionRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSessionRefUserPageVO;

public interface BaseImSessionRefUserService extends IService<BaseImSessionRefUserDO> {

    void addSessionRefUserForFriend(Long sessionId, Long userId1, Long userId2);

    Page<BaseImSessionRefUserPageVO> myPage(BaseImSessionRefUserPageDTO dto);

    String hidden(NotNullId dto);

}
