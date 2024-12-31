package com.kar20240901.be.base.web.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.file.BaseFileTransferMapper;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferDO;
import com.kar20240901.be.base.web.service.file.BaseFileTransferService;
import org.springframework.stereotype.Service;

@Service
public class BaseFileTransferServiceImpl extends ServiceImpl<BaseFileTransferMapper, BaseFileTransferDO>
    implements BaseFileTransferService {

}
