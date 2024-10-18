package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseMenuDO;
import com.kar20240901.be.base.web.model.dto.base.BaseMenuInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseMenuPageDTO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseMenuInfoByIdVO;
import java.util.List;

public interface BaseMenuService extends IService<BaseMenuDO> {

    String insertOrUpdate(BaseMenuInsertOrUpdateDTO dto);

    Page<BaseMenuDO> myPage(BaseMenuPageDTO dto);

    List<BaseMenuDO> tree(BaseMenuPageDTO dto);

    List<BaseMenuDO> dictTreeList();

    BaseMenuInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    List<BaseMenuDO> userSelfMenuList();

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
