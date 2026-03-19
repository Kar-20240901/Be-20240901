package com.kar20240901.be.base.web.service.base.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.base.BaseUserInfoMapper;
import com.kar20240901.be.base.web.model.domain.base.TempUserInfoDO;
import com.kar20240901.be.base.web.service.base.BaseUserInfoService;
import org.springframework.stereotype.Service;

@Service
public class BaseUserInfoServiceImpl extends ServiceImpl<BaseUserInfoMapper, TempUserInfoDO>
    implements BaseUserInfoService {

}
