package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyFriendMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyFriendDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyFriendSendDTO;
import com.kar20240901.be.base.web.service.im.BaseImApplyFriendService;
import org.springframework.stereotype.Service;

@Service
public class BaseImApplyFriendServiceImpl extends ServiceImpl<BaseImApplyFriendMapper, BaseImApplyFriendDO>
    implements BaseImApplyFriendService {

    /**
     * 发送好友申请
     */
    @Override
    public String send(BaseImApplyFriendSendDTO dto) {

        return TempBizCodeEnum.OK;

    }

}
