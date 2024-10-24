package com.kar20240901.be.base.web.service.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.request.SysRequestDO;
import com.kar20240901.be.base.web.model.dto.request.SysRequestPageDTO;
import com.kar20240901.be.base.web.model.dto.request.SysRequestSelfLoginRecordPageDTO;
import com.kar20240901.be.base.web.model.vo.request.SysRequestAllAvgVO;

public interface SysRequestService extends IService<SysRequestDO> {

    Page<SysRequestDO> myPage(SysRequestPageDTO dto);

    SysRequestAllAvgVO allAvgPro(SysRequestPageDTO dto);

    Page<SysRequestDO> selfLoginRecord(SysRequestSelfLoginRecordPageDTO dto);

}
