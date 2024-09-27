package com.kar20240901.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.BaseAuthDO;
import com.kar20240901.be.base.web.model.dto.BaseAuthInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.BaseAuthPageDTO;
import com.kar20240901.be.base.web.model.vo.BaseAuthInfoByIdVO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.NotNullId;
import com.kar20240901.be.base.web.model.vo.DictVO;

public interface BaseAuthService extends IService<BaseAuthDO> {

    String insertOrUpdate(BaseAuthInsertOrUpdateDTO dto);

    Page<BaseAuthDO> myPage(BaseAuthPageDTO dto);

    Page<DictVO> dictList();

    BaseAuthInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
