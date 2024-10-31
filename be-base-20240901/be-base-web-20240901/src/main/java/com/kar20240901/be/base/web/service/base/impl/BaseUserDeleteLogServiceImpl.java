package com.kar20240901.be.base.web.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.base.BaseUserDeleteLogMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserDeleteLogDO;
import com.kar20240901.be.base.web.service.base.BaseUserDeleteLogService;
import org.springframework.stereotype.Service;

@Service
public class BaseUserDeleteLogServiceImpl extends ServiceImpl<BaseUserDeleteLogMapper, BaseUserDeleteLogDO>
    implements BaseUserDeleteLogService {

}
