package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseAuthDO;
import com.kar20240901.be.base.web.model.dto.base.BaseAuthInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseAuthPageDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseAuthInfoByIdVO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;

public interface BaseAuthService extends IService<BaseAuthDO> {

    String insertOrUpdate(BaseAuthInsertOrUpdateDTO dto);

    Page<BaseAuthDO> myPage(BaseAuthPageDTO dto);

    Page<DictVO> dictList();

    BaseAuthInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

}
