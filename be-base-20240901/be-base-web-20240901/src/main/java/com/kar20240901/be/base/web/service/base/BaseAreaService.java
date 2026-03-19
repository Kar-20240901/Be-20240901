package com.kar20240901.be.base.web.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.base.BaseAreaDO;
import com.kar20240901.be.base.web.model.dto.base.BaseAreaInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.base.BaseAreaPageDTO;
import com.kar20240901.be.base.web.model.dto.base.ChangeNumberDTO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.vo.base.BaseAreaInfoByIdVO;
import java.util.List;

public interface BaseAreaService extends IService<BaseAreaDO> {

    String insertOrUpdate(BaseAreaInsertOrUpdateDTO dto);

    Page<BaseAreaDO> myPage(BaseAreaPageDTO dto);

    List<BaseAreaDO> tree(BaseAreaPageDTO dto);

    List<BaseAreaDO> dictTreeList();

    BaseAreaInfoByIdVO infoById(NotNullId notNullId);

    String deleteByIdSet(NotEmptyIdSet notEmptyIdSet);

    String addOrderNo(ChangeNumberDTO dto);

    String updateOrderNo(ChangeNumberDTO dto);

}
