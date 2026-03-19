package com.kar20240901.be.base.web.util.base;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.base.BaseSignConfigurationMapper;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.enums.base.BaseRedisKeyEnum;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class BaseUserConfigurationUtil {

    private static BaseSignConfigurationMapper baseSignConfigurationMapper;

    @Resource
    public void setBaseSignConfigurationMapper(BaseSignConfigurationMapper baseSignConfigurationMapper) {
        BaseUserConfigurationUtil.baseSignConfigurationMapper = baseSignConfigurationMapper;
    }

    private static RedissonClient redissonClient;

    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        BaseUserConfigurationUtil.redissonClient = redissonClient;
    }

    /**
     * 获取：用户登录注册相关配置
     */
    @NotNull
    public static BaseUserConfigurationDO getBaseUserConfigurationDo() {

        RBucket<BaseUserConfigurationDO> rBucket =
            redissonClient.getBucket(BaseRedisKeyEnum.SIGN_CONFIGURATION_CACHE.name());

        BaseUserConfigurationDO baseUserConfigurationDO = rBucket.get();

        if (baseUserConfigurationDO != null) {
            return baseUserConfigurationDO;
        }

        // 从数据库中获取
        baseUserConfigurationDO = doGetBaseUserConfigurationDo();

        // 设置：缓存
        rBucket.set(baseUserConfigurationDO);

        return baseUserConfigurationDO;

    }

    /**
     * 执行
     */
    @NotNull
    private static BaseUserConfigurationDO doGetBaseUserConfigurationDo() {

        BaseUserConfigurationDO baseUserConfigurationDO =
            ChainWrappers.lambdaQueryChain(baseSignConfigurationMapper).one();

        if (baseUserConfigurationDO == null) {

            baseUserConfigurationDO = RedissonUtil.doLock(BaseRedisKeyEnum.PRE_SIGN_CONFIGURATION.name(), () -> {

                // 这里需要再查询一次
                BaseUserConfigurationDO tempBaseUserConfigurationDO =
                    ChainWrappers.lambdaQueryChain(baseSignConfigurationMapper).one();

                if (tempBaseUserConfigurationDO != null) {
                    return tempBaseUserConfigurationDO;
                }

                tempBaseUserConfigurationDO = new BaseUserConfigurationDO();

                tempBaseUserConfigurationDO.setUserNameSignUpEnable(true);
                tempBaseUserConfigurationDO.setEmailSignUpEnable(true);
                tempBaseUserConfigurationDO.setPhoneSignUpEnable(true);
                tempBaseUserConfigurationDO.setManageOperateEnable(true);
                tempBaseUserConfigurationDO.setNormalOperateEnable(true);

                // 保存：用户登录注册相关配置
                baseSignConfigurationMapper.insert(tempBaseUserConfigurationDO);

                return tempBaseUserConfigurationDO;

            });

        }

        return baseUserConfigurationDO;

    }

}
