package com.kar20240901.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.BaseUserInfoMapper;
import com.kar20240901.be.base.web.service.BaseUserInfoService;
import com.kar20240901.be.base.web.model.domain.BaseUserInfoDO;
import org.springframework.stereotype.Service;

@Service
public class BaseUserInfoServiceImpl extends ServiceImpl<BaseUserInfoMapper, BaseUserInfoDO>
    implements BaseUserInfoService {

}
