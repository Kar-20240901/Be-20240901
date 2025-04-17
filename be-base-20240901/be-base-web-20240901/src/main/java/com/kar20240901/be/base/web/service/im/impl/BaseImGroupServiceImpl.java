package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
import org.springframework.stereotype.Service;

@Service
public class BaseImGroupServiceImpl extends ServiceImpl<BaseImGroupMapper, BaseImGroupDO>
    implements BaseImGroupService {

}
