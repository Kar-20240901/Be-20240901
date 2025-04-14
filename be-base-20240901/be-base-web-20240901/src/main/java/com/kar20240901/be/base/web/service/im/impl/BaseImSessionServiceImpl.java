package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionDO;
import com.kar20240901.be.base.web.service.im.BaseImSessionService;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionServiceImpl extends ServiceImpl<BaseImSessionMapper, BaseImSessionDO>
    implements BaseImSessionService {

}
