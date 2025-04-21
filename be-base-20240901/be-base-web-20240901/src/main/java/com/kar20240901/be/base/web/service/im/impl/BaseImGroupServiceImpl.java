package com.kar20240901.be.base.web.service.im.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImGroupRefUserMapper;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImGroupRefUserDO;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.im.BaseImGroupRemoveDTO;
import com.kar20240901.be.base.web.model.enums.im.BaseImTypeEnum;
import com.kar20240901.be.base.web.model.vo.base.R;
import com.kar20240901.be.base.web.service.im.BaseImGroupService;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseImGroupServiceImpl extends ServiceImpl<BaseImGroupMapper, BaseImGroupDO>
    implements BaseImGroupService {

    @Resource
    BaseImGroupRefUserMapper baseImGroupRefUserMapper;

    @Resource
    BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    /**
     * 踢出群员
     */
    @Override
    @MyTransactional
    public String removeUser(BaseImGroupRemoveDTO dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        boolean exists =
            lambdaQuery().eq(BaseImGroupDO::getBelongId, currentUserId).eq(BaseImGroupDO::getId, dto.getGroupId())
                .exists();

        if (!exists) {
            R.error("操作失败：只能群主进行该操作", dto.getGroupId());
        }

        ChainWrappers.lambdaUpdateChain(baseImGroupRefUserMapper).eq(BaseImGroupRefUserDO::getGroupId, dto.getGroupId())
            .eq(BaseImGroupRefUserDO::getUserId, dto.getUserId()).remove();

        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper)
            .eq(BaseImSessionRefUserDO::getTargetId, dto.getGroupId())
            .eq(BaseImSessionRefUserDO::getTargetType, BaseImTypeEnum.GROUP)
            .eq(BaseImSessionRefUserDO::getUserId, dto.getUserId()).remove();

        return TempBizCodeEnum.OK;

    }

}
