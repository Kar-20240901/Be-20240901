package com.kar20240901.be.base.web.configuration.security;

import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.model.interfaces.IJwtFilterHandler;
import com.kar20240901.be.base.web.util.MyUserInfoUtil;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class BaseJwtFilterHandler implements IJwtFilterHandler {

    @Override
    public void handleJwt(Long userId, String ip, JWT jwt) {

        MyUserInfoUtil.updateUserInfo(userId, new Date(), ip);

    }

}
