package com.kar20240901.be.base.web.configuration.wallet;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.wallet.BaseUserWalletMapper;
import com.kar20240901.be.base.web.model.configuration.base.IUserSignConfiguration;
import com.kar20240901.be.base.web.model.domain.wallet.BaseUserWalletDO;
import java.math.BigDecimal;
import java.util.Set;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BaseUserWalletUserSignConfiguration implements IUserSignConfiguration {

    @Resource
    BaseUserWalletMapper baseUserWalletMapper;

    @Override
    public Object signUp(@NotNull Long userId) {

        BaseUserWalletDO baseUserWalletDO = getInitSysUserWalletDO(userId);

        baseUserWalletMapper.insert(baseUserWalletDO);

        return baseUserWalletDO;

    }

    /**
     * 获取：一个初始的 SysUserWalletDO对象
     */
    @NotNull
    public static BaseUserWalletDO getInitSysUserWalletDO(@NotNull Long userId) {

        BaseUserWalletDO baseUserWalletDO = new BaseUserWalletDO();

        baseUserWalletDO.setId(userId);

        baseUserWalletDO.setWithdrawableMoney(BigDecimal.ZERO);
        baseUserWalletDO.setWithdrawablePreUseMoney(BigDecimal.ZERO);

        baseUserWalletDO.setEnableFlag(true);
        baseUserWalletDO.setRemark("");

        return baseUserWalletDO;

    }

    @Override
    public void delete(Set<Long> userIdSet) {

        ChainWrappers.lambdaUpdateChain(baseUserWalletMapper).in(BaseUserWalletDO::getId, userIdSet).remove();

    }

}
