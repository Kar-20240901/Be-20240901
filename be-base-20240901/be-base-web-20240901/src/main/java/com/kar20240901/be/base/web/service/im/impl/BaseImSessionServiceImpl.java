package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionDO;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImType;
import com.kar20240901.be.base.web.service.im.BaseImSessionService;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionServiceImpl extends ServiceImpl<BaseImSessionMapper, BaseImSessionDO>
    implements BaseImSessionService {

    /**
     * 新增会话
     */
    @Override
    public void addSession(Long sessionId, Long sourceApplyId, IBaseImType iBaseImType) {

        Assert.notNull(sessionId);

        Assert.notNull(sourceApplyId);

        Assert.notNull(iBaseImType);

        BaseImSessionDO baseImSessionDO = new BaseImSessionDO();

        baseImSessionDO.setId(sessionId);

        baseImSessionDO.setSourceApplyId(sourceApplyId);

        baseImSessionDO.setSourceApplyType(iBaseImType.getCode());

        baseImSessionDO.setLastReceiveTs(TempConstant.NEGATIVE_ONE);

        save(baseImSessionDO);

    }

}
