package com.kar20240901.be.base.web.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmcorg20230301.be.engine.security.mapper.SysUserDeleteLogMapper;
import com.cmcorg20230301.be.engine.security.model.entity.SysUserDeleteLogDO;
import com.kar20240901.be.base.web.service.base.SysUserDeleteLogService;
import org.springframework.stereotype.Service;

@Service
public class SysUserDeleteLogServiceImpl extends ServiceImpl<SysUserDeleteLogMapper, SysUserDeleteLogDO>
    implements SysUserDeleteLogService {

}
