package com.kar20240901.be.base.web.model.interfaces;

import cn.hutool.jwt.JWT;

public interface IJwtFilterHandler {

    void handleJwt(Long userId, String ip, JWT jwt);

}
