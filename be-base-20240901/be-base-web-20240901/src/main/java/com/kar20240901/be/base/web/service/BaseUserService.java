package com.kar20240901.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.TempUserDO;
import com.kar20240901.be.base.web.model.dto.BaseUserInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.BaseUserPageDTO;
import com.kar20240901.be.base.web.model.dto.BaseUserUpdatePasswordDTO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.NotNullId;
import com.kar20240901.be.base.web.model.vo.BaseUserPageVO;
import com.kar20240901.be.base.web.model.vo.DictVO;
import com.kar20240901.be.base.web.model.vo.TempUserInfoByIdVO;

public interface BaseUserService extends IService<TempUserDO> {

    Page<BaseUserPageVO> myPage(BaseUserPageDTO dto);

    Page<DictVO> dictList();

    String insertOrUpdate(BaseUserInsertOrUpdateDTO dto);

    TempUserInfoByIdVO infoById(NotNullId notNullId);

    Boolean manageSignInFlag();

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String resetAvatar(NotEmptyIdSet notEmptyIdSet);

    String updatePassword(BaseUserUpdatePasswordDTO dto);

    String thaw(NotEmptyIdSet notEmptyIdSet);

    String freeze(NotEmptyIdSet notEmptyIdSet);

}
