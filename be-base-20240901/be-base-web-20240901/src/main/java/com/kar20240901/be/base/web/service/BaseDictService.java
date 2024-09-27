package com.kar20240901.be.base.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.dto.BaseDictInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.BaseDictListByDictKeyDTO;
import com.kar20240901.be.base.web.model.dto.BaseDictPageDTO;
import com.kar20240901.be.base.web.model.domain.BaseDictDO;
import com.kar20240901.be.base.web.model.dto.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.NotNullId;
import com.kar20240901.be.base.web.model.vo.DictIntegerVO;
import java.util.List;

public interface BaseDictService extends IService<BaseDictDO> {

    String insertOrUpdate(BaseDictInsertOrUpdateDTO dto);

    Page<BaseDictDO> myPage(BaseDictPageDTO dto);

    List<DictIntegerVO> listByDictKey(BaseDictListByDictKeyDTO dto);

    List<BaseDictDO> tree(BaseDictPageDTO dto);

    BaseDictDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkDeleteFlag);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
