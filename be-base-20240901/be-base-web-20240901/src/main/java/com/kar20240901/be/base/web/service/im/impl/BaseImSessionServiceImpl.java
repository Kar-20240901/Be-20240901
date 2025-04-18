package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionDO;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImType;
import com.kar20240901.be.base.web.service.im.BaseImSessionService;
import com.kar20240901.be.base.web.util.base.IdGeneratorUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionServiceImpl extends ServiceImpl<BaseImSessionMapper, BaseImSessionDO>
    implements BaseImSessionService {

    /**
     * 新增会话
     */
    @Override
    public void addSession(@Nullable Long sessionId, Long sourceApplyId, IBaseImType iBaseImType) {

        Assert.notNull(sourceApplyId);

        Assert.notNull(iBaseImType);

        if (sessionId == null) {

            sessionId = IdGeneratorUtil.nextId();

        }

        BaseImSessionDO baseImSessionDO = new BaseImSessionDO();

        baseImSessionDO.setId(sessionId);

        baseImSessionDO.setSourceApplyId(sourceApplyId);

        baseImSessionDO.setSourceApplyType(iBaseImType.getCode());

        save(baseImSessionDO);

    }

}
