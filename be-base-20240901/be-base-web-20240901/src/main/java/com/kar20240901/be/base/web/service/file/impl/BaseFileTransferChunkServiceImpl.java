package com.kar20240901.be.base.web.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.file.BaseFileTransferChunkMapper;
import com.kar20240901.be.base.web.model.domain.file.BaseFileTransferChunkDO;
import com.kar20240901.be.base.web.service.file.BaseFileTransferChunkService;
import org.springframework.stereotype.Service;

@Service
public class BaseFileTransferChunkServiceImpl extends ServiceImpl<BaseFileTransferChunkMapper, BaseFileTransferChunkDO>
    implements BaseFileTransferChunkService {

}
