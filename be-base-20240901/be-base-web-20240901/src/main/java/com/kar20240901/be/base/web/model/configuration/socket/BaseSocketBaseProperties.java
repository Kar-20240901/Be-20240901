package com.kar20240901.be.base.web.model.configuration.socket;

import lombok.Data;

/**
 * socket配置类，基础类
 */
@Data
public class BaseSocketBaseProperties {

    /**
     * 协议：例如：ws://，wss:// 等
     */
    private String scheme;

    /**
     * 主机：例如：ip，域名，等
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 路径，备注：以 / 开头
     */
    private String path;

    /**
     * parentGroup的线程池大小
     */
    private Integer parentSize = 1;

    /**
     * childGroup的线程池大小
     */
    private Integer childSize = Runtime.getRuntime().availableProcessors() * 10;

}
