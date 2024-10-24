package com.kar20240901.be.base.web.service.request.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.request.BaseRequestInfoMapper;
import com.kar20240901.be.base.web.model.domain.request.BaseRequestInfoDO;
import com.kar20240901.be.base.web.service.request.BaseRequestInfoService;
import org.springframework.stereotype.Service;

@Service
public class BaseRequestInfoServiceImpl extends ServiceImpl<BaseRequestInfoMapper, BaseRequestInfoDO>
    implements BaseRequestInfoService {

}
