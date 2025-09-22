package com.kar20240901.be.base.web.model.interfaces.base;

import cn.hutool.jwt.JWT;
import javax.servlet.http.HttpServletRequest;

public interface IJwtFilterHandler {

    void handleJwt(Long userId, String ip, JWT jwt, HttpServletRequest request);

}
