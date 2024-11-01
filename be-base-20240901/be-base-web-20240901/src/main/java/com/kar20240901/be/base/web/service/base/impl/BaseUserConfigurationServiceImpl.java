package com.kar20240901.be.base.web.service.base.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.base.BaseSignConfigurationMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.dto.base.BaseUserConfigurationInsertOrUpdateDTO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import com.kar20240901.be.base.web.service.base.BaseUserConfigurationService;
import com.kar20240901.be.base.web.util.base.RedissonUtil;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class BaseUserConfigurationServiceImpl extends ServiceImpl<BaseSignConfigurationMapper, BaseUserConfigurationDO>
    implements BaseUserConfigurationService {

    /**
     * 获取：用户登录注册相关配置
     */
    @NotNull
    @Override
    public BaseUserConfigurationDO getBaseUserConfigurationDo() {

        BaseUserConfigurationDO baseUserConfigurationDO = lambdaQuery().one();

        if (baseUserConfigurationDO == null) {

            baseUserConfigurationDO = RedissonUtil.doLock(BaseRedisKeyEnum.PRE_SIGN_CONFIGURATION.name(), () -> {

                // 这里需要再查询一次
                BaseUserConfigurationDO tempBaseUserConfigurationDO = lambdaQuery().one();

                if (tempBaseUserConfigurationDO != null) {
                    return tempBaseUserConfigurationDO;
                }

                tempBaseUserConfigurationDO = new BaseUserConfigurationDO();

                tempBaseUserConfigurationDO.setUserNameSignUpEnable(true);
                tempBaseUserConfigurationDO.setEmailSignUpEnable(true);
                tempBaseUserConfigurationDO.setPhoneSignUpEnable(true);

                save(tempBaseUserConfigurationDO); // 保存：用户登录注册相关配置

                return tempBaseUserConfigurationDO;

            });

        }

        return baseUserConfigurationDO;

    }

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

        return getBaseUserConfigurationDo();

    }

}
