package com.kar20240901.be.base.web.service.thirdapp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.thirdapp.BaseThirdAppOfficialMenuDO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppOfficialMenuInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.thirdapp.BaseThirdAppOfficialMenuPageDTO;
import java.util.List;

public interface BaseThirdAppOfficialMenuService extends IService<BaseThirdAppOfficialMenuDO> {

    String insertOrUpdate(BaseThirdAppOfficialMenuInsertOrUpdateDTO dto);

    Page<BaseThirdAppOfficialMenuDO> myPage(BaseThirdAppOfficialMenuPageDTO dto);

    List<BaseThirdAppOfficialMenuDO> tree(BaseThirdAppOfficialMenuPageDTO dto);

    BaseThirdAppOfficialMenuDO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

}
