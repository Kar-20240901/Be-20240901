package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserAddMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserDeleteMuteDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserMutePageDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRefUserPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImGroupRefUserPageVO;

public interface BaseImGroupRefUserService extends IService<BaseImGroupRefUserDO> {

    Page<BaseImGroupRefUserPageVO> myPage(BaseImGroupRefUserMutePageDTO dto);

    Page<BaseImGroupRefUserPageVO> pageMute(BaseImGroupRefUserPageDTO dto);

    String addMute(BaseImGroupRefUserAddMuteDTO dto);

    String deleteMute(BaseImGroupRefUserDeleteMuteDTO dto);

    void addUser(Long sessionId, Long groupId, Long userId);

}
