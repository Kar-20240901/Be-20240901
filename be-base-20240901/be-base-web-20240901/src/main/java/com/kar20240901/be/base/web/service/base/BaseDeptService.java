package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseDeptDO;
import com.kar20240901.be.base.web.model.dto.base.BaseDeptInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseDeptPageDTO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseDeptInfoByIdVO;
import java.util.List;

public interface BaseDeptService extends IService<BaseDeptDO> {

    String insertOrUpdate(BaseDeptInsertOrUpdateDTO dto);

    Page<BaseDeptDO> myPage(BaseDeptPageDTO dto);

    List<BaseDeptDO> tree(BaseDeptPageDTO dto);

    List<BaseDeptDO> dictTreeList();

    BaseDeptInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
