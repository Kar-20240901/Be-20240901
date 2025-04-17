package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImApplyGroupMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImApplyGroupDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.dto.im.BaseImApplyGroupSendDTO;
import com.kar20240901.be.base.web.service.im.BaseImApplyGroupService;
import org.springframework.stereotype.Service;

@Service
public class BaseImApplyGroupServiceImpl extends ServiceImpl<BaseImApplyGroupMapper, BaseImApplyGroupDO>
    implements BaseImApplyGroupService {

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseImApplyGroupInsertOrUpdateDTO dto) {

        return TempBizCodeEnum.OK;

    }

    /**
     * 发送群组申请
     */
    @Override
    public String send(BaseImApplyGroupSendDTO dto) {

        return TempBizCodeEnum.OK;

    }

}
