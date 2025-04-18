package com.kar20240901.be.base.web.service.im;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionDO;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImType;

public interface BaseImSessionService extends IService<BaseImSessionDO> {

    void addSession(Long sessionId, Long sourceApplyId, IBaseImType iBaseImType);

}
