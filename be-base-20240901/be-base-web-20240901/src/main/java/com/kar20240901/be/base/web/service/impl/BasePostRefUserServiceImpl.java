package com.kar20240901.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.BasePostRefUserMapper;
import com.kar20240901.be.base.web.model.domain.BasePostRefUserDO;
import com.kar20240901.be.base.web.service.BasePostRefUserService;
import org.springframework.stereotype.Service;

@Service
public class BasePostRefUserServiceImpl extends ServiceImpl<BasePostRefUserMapper, BasePostRefUserDO>
    implements BasePostRefUserService {

}
