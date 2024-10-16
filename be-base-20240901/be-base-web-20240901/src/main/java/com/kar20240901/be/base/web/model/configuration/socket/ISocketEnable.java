package com.kar20240901.be.base.web.model.configuration.socket;

import java.util.Set;

public interface ISocketEnable {

    /**
     * 执行处理
     */
    void handle(Set<Long> socketIdSet);

}
