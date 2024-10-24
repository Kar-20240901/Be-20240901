package com.kar20240901.be.base.web.service.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestDO;
import com.kar20240901.be.base.web.model.dto.request.BaseRequestPageDTO;
import com.kar20240901.be.base.web.model.dto.request.BaseRequestSelfLoginRecordPageDTO;
import com.kar20240901.be.base.web.model.vo.request.SysRequestAllAvgVO;

public interface BaseRequestService extends IService<BaseRequestDO> {

    Page<BaseRequestDO> myPage(BaseRequestPageDTO dto);

    SysRequestAllAvgVO allAvgPro(BaseRequestPageDTO dto);

    Page<BaseRequestDO> selfLoginRecord(BaseRequestSelfLoginRecordPageDTO dto);

}
