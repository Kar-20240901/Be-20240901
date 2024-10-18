package com.kar20240901.be.base.web.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.base.BaseSignConfigurationMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.service.base.BaseUserConfigurationService;
import org.springframework.stereotype.Service;

@Service
public class BaseUserConfigurationServiceImpl extends ServiceImpl<BaseSignConfigurationMapper, BaseUserConfigurationDO>
    implements BaseUserConfigurationService {
}
