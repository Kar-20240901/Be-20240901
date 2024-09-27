package com.kar20240901.be.base.web.security;

import cn.hutool.jwt.JWT;
import com.kar20240901.be.base.web.model.interfaces.IJwtFilterHandler;
import org.springframework.stereotype.Component;

@Component
public class BaseJwtFilterHandler implements IJwtFilterHandler {

    @Override
    public void handleJwt(Long userId, String ip, JWT jwt) {

    }

}
