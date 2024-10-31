package com.kar20240901.be.base.web.service.otherapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.otherapp.BaseOtherAppOfficialAccountMenuDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import java.util.List;

public interface BaseOtherAppOfficialAccountMenuService extends IService<BaseOtherAppOfficialAccountMenuDO> {

    String insertOrUpdate(BaseOtherAppOfficialAccountMenuInsertOrUpdateDTO dto);

    Page<BaseOtherAppOfficialAccountMenuDO> myPage(BaseOtherAppOfficialAccountMenuPageDTO dto);

    List<BaseOtherAppOfficialAccountMenuDO> tree(BaseOtherAppOfficialAccountMenuPageDTO dto);

    BaseOtherAppOfficialAccountMenuDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

}
