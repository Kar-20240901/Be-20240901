package com.kar20240901.be.base.web.configuration.security;

import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.model.interfaces.base.IJwtFilterHandler;
import com.kar20240901.be.base.web.util.base.MyUserInfoUtil;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class BaseJwtFilterHandler implements IJwtFilterHandler {

    @Override
    public void handleJwt(Long userId, String ip, JWT jwt) {

        MyUserInfoUtil.add(userId, new Date(), ip, null);

    }

}
