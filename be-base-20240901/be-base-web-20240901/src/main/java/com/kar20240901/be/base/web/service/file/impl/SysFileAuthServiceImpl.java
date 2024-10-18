package com.kar20240901.be.base.web.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.file.SysFileAuthMapper;
import com.kar20240901.be.base.web.model.domain.file.SysFileAuthDO;
import com.kar20240901.be.base.web.service.file.SysFileAuthService;
import org.springframework.stereotype.Service;

@Service
public class SysFileAuthServiceImpl extends ServiceImpl<SysFileAuthMapper, SysFileAuthDO>
    implements SysFileAuthService {

}
