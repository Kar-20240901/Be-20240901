package com.kar20240901.be.base.web.service.pay;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.pay.BasePayConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.pay.BasePayConfigurationPageDTO;
import com.kar20240901.be.base.web.model.vo.base.DictVO;

public interface BasePayConfigurationService extends IService<BasePayConfigurationDO> {

    String insertOrUpdate(BasePayConfigurationInsertOrUpdateDTO dto);

    Page<BasePayConfigurationDO> myPage(BasePayConfigurationPageDTO dto);

    Page<DictVO> dictList();

    BasePayConfigurationDO infoById(NotNullId dto);

    String deleteByIdSet(NotEmptyIdSet dto);

}
