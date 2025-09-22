package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseSignConfigurationMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.service.base.BaseUserConfigurationService;
import com.kar20240901.be.base.web.util.base.BaseUserConfigurationUtil;
import org.springframework.stereotype.Service;

@Service
public class BaseUserConfigurationServiceImpl extends ServiceImpl<BaseSignConfigurationMapper, BaseUserConfigurationDO>
    implements BaseUserConfigurationService {

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseUserConfigurationInsertOrUpdateDTO dto) {

        BaseUserConfigurationDO baseUserConfigurationDO = lambdaQuery().one();

        boolean insertFlag = baseUserConfigurationDO == null;

        if (insertFlag) {

            baseUserConfigurationDO = new BaseUserConfigurationDO();

        }

        baseUserConfigurationDO.setUserNameSignUpEnable(BooleanUtil.isTrue(dto.getUserNameSignUpEnable()));
        baseUserConfigurationDO.setEmailSignUpEnable(BooleanUtil.isTrue(dto.getEmailSignUpEnable()));
        baseUserConfigurationDO.setPhoneSignUpEnable(BooleanUtil.isTrue(dto.getPhoneSignUpEnable()));
        baseUserConfigurationDO.setManageSignInEnable(BooleanUtil.isTrue(dto.getManageSignInEnable()));
        baseUserConfigurationDO.setNormalSignInEnable(BooleanUtil.isTrue(dto.getNormalSignInEnable()));

        if (insertFlag) {

            save(baseUserConfigurationDO);

        } else {

            updateById(baseUserConfigurationDO);

        }

        return TempBizCodeEnum.OK;

    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseUserConfigurationDO infoById() {

        return BaseUserConfigurationUtil.getBaseUserConfigurationDo();

    }

}
