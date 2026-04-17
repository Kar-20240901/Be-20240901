package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240901.be.base.web.model.dto.base.NotNullId;
import com.kar20240901.be.base.web.model.dto.base.ScrollListDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchBaseDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImSearchHistoryPageDTO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseContentVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchBaseVO;
import com.kar20240901.be.base.web.model.vo.im.BaseImSearchHistoryVO;
import java.util.List;

public interface BaseImSearchService {

    Page<BaseImSearchHistoryVO> searchHistoryPage(BaseImSearchHistoryPageDTO dto);

    String searchHistoryDelete(NotNullId dto);

    String searchHistoryDeleteAll();

    BaseImSearchBaseVO searchBase(BaseImSearchBaseDTO dto);

    List<BaseImSearchBaseContentVO> baseContentScroll(ScrollListDTO dto);

}
