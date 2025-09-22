package com.kar20240901.be.base.web.model.interfaces.base;

import cn.hutool.jwt.JWT;
import javax.servlet.http.HttpServletRequest;

public interface IJwtFilterHandler {

    // null表示成功，反之表示错误
    IBizCode handleJwt(Long userId, String ip, JWT jwt, HttpServletRequest request);

}
