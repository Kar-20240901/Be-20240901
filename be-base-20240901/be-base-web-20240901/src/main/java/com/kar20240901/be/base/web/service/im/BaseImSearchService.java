package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchBaseDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchHistoryPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchHistoryVO;

public interface BaseImSearchService {

    Page<BaseImSearchHistoryVO> searchHistoryPage(BaseImSearchHistoryPageDTO dto);

    String searchHistoryDelete(NotNullId dto);

    String searchHistoryDeleteAll();

    BaseImSearchBaseVO searchBase(BaseImSearchBaseDTO dto);

}
