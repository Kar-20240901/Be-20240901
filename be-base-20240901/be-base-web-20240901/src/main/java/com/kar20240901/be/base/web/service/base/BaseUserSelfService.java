package com.kar20240901.be.base.web.service.base;

import com.kar20240901.be.base.web.model.dto.base.BaseUserSelfUpdateInfoDTO;
import com.kar20240901.be.base.web.model.dto.base.NotBlankString;
import com.kar20240901.be.base.web.model.vo.base.BaseUserSelfInfoVO;

public interface BaseUserSelfService {

    BaseUserSelfInfoVO userSelfInfo();

    String userSelfUpdateInfo(BaseUserSelfUpdateInfoDTO dto);

    String userSelfResetAvatar();

    String updateUuid(NotBlankString dto);

}
