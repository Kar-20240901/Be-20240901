package com.kar20240901.be.base.web.model.constant.base;

/**
 * Properties配置的前缀常量类
 */
public interface PropertiesPrefixConstant {

    String PRE_BE = "be.";

    String SECURITY = PRE_BE + "security"; // 权限相关

    String COMMON = PRE_BE + "common"; // 通用相关

    String LOG = PRE_BE + "log"; // 日志相关

    String CACHE = PRE_BE + "cache"; // 缓存相关

    String FILE = PRE_BE + "file"; // 文件相关

    String FILE_TYPE = FILE + ".type"; // 文件类型相关

    String SOCKET = PRE_BE + "socket"; // socket相关

    String SOCKET_WEB_SOCKET = SOCKET + ".web-socket"; // webSocket相关

    String SOCKET_WEB_TCP_PROTOBUF = SOCKET + ".tcp-protobuf"; // tcp-protobuf相关

    String THIRD_APP = PRE_BE + "third-app"; // 第三方应用相关

    String OFFICIAL = THIRD_APP + ".official"; // 第三方应用-公众号相关

}
