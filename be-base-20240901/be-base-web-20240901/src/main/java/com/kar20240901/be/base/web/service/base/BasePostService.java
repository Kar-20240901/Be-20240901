package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BasePostDO;
import com.kar20240901.be.base.web.model.dto.base.BasePostInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BasePostPageDTO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BasePostInfoByIdVO;
import java.util.List;

public interface BasePostService extends IService<BasePostDO> {

    String insertOrUpdate(BasePostInsertOrUpdateDTO dto);

    Page<BasePostDO> myPage(BasePostPageDTO dto);

    List<BasePostDO> tree(BasePostPageDTO dto);

    List<BasePostDO> dictTreeList();

    BasePostInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
