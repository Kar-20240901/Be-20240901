package com.kar20240901.be.base.web.service.bulletin.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.bulletin.BaseBulletinReadTimeRefUserMapper;
import com.kar20240901.be.base.web.model.domain.bulletin.BaseBulletinReadTimeRefUserDO;
import com.kar20240901.be.base.web.service.bulletin.BaseBulletinReadTimeRefUserService;
import org.springframework.stereotype.Service;

@Service
public class BaseBulletinReadTimeRefUserServiceImpl
    extends ServiceImpl<BaseBulletinReadTimeRefUserMapper, BaseBulletinReadTimeRefUserDO>
    implements BaseBulletinReadTimeRefUserService {

}
