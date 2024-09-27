package com.kar20240901.be.base.web.model.configuration;

import cn.hutool.jwt.JWT;
import java.util.Set;

public interface IJwtGetAuthSetConfiguration {

    /**
     * 获取：权限 set
     */
    Set<String> getAuthSet(Long userId, JWT jwt);

}
