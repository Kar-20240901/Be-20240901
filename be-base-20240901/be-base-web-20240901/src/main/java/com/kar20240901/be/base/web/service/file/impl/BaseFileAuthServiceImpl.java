package com.kar20240901.be.base.web.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.file.BaseFileAuthMapper;
import com.kar20240901.be.base.web.model.domain.file.BaseFileAuthDO;
import com.kar20240901.be.base.web.service.file.BaseFileAuthService;
import org.springframework.stereotype.Service;

@Service
public class BaseFileAuthServiceImpl extends ServiceImpl<BaseFileAuthMapper, BaseFileAuthDO>
    implements BaseFileAuthService {

}
