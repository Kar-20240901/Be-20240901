package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseRoleDO;
import com.kar20240901.be.base.web.model.dto.base.BaseRoleInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseRolePageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseRoleInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;

public interface BaseRoleService extends IService<BaseRoleDO> {

    String insertOrUpdate(BaseRoleInsertOrUpdateDTO dto);

    Page<BaseRoleDO> myPage(BaseRolePageDTO dto);

    Page<DictVO> dictList();

    BaseRoleInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
