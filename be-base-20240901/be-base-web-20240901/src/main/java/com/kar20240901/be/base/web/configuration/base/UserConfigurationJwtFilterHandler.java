package com.kar20240901.be.base.web.configuration.base;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.exception.base.BaseBizCodeEnum;
import com.kar20240901.be.base.web.model.domain.base.BaseUserConfigurationDO;
import com.kar20240901.be.base.web.model.interfaces.base.IBizCode;
import com.kar20240901.be.base.web.model.interfaces.base.IJwtFilterHandler;
import com.kar20240901.be.base.web.util.base.BaseUserConfigurationUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConfigurationJwtFilterHandler implements IJwtFilterHandler {

    @Override
    public IBizCode handleJwt(Long userId, String ip, JWT jwt, HttpServletRequest request) {

        if (MyUserUtil.getCurrentUserSuperAdminFlag(userId)) {
            return null;
        }

        BaseUserConfigurationDO baseUserConfigurationDO = BaseUserConfigurationUtil.getBaseUserConfigurationDo();

        if (MyUserUtil.getCurrentUserAdminFlag()) {

            if (BooleanUtil.isFalse(baseUserConfigurationDO.getManageOperateEnable())) {

                return BaseBizCodeEnum.MANAGE_OPERATE_NOT_ENABLE;

            }

        } else {

            if (BooleanUtil.isFalse(baseUserConfigurationDO.getNormalOperateEnable())) {

                return BaseBizCodeEnum.NORMAL_OPERATE_NOT_ENABLE;

            }

        }

        return null;

    }

}
