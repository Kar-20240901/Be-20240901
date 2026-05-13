package com.kar20240901.be.base.web.util.im;

import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.exception.TempBizCodeEnum;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionRefUserMapper;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionRefUserDO;
import com.kar20240901.be.base.web.model.dto.base.NotEmptyIdSet;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaseImUtil {

    private static BaseImSessionRefUserMapper baseImSessionRefUserMapper;

    @Resource
    public void setBaseImSessionRefUserMapper(BaseImSessionRefUserMapper baseImSessionRefUserMapper) {
        BaseImUtil.baseImSessionRefUserMapper = baseImSessionRefUserMapper;
    }

    /**
     * 隐藏会话
     */
    public static String hiddenSessionRefUser(NotEmptyIdSet dto) {

        Long currentUserId = MyUserUtil.getCurrentUserId();

        ChainWrappers.lambdaUpdateChain(baseImSessionRefUserMapper).eq(BaseImSessionRefUserDO::getUserId, currentUserId)
            .in(BaseImSessionRefUserDO::getSessionId, dto.getIdSet()).set(BaseImSessionRefUserDO::getShowFlag, false)
            .update();

        return TempBizCodeEnum.OK;

    }

}
