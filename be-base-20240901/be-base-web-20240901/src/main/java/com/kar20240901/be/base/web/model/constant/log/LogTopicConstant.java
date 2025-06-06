package com.kar20240901.be.base.web.model.constant.log;

import com.kar20240901.be.base.web.model.constant.base.PropertiesPrefixConstant;

/**
 * 日志主题的常量类
 */
public interface LogTopicConstant {

    String PRE_BE = PropertiesPrefixConstant.PRE_BE;

    String SQL = PRE_BE + "sql"; // sql相关

    String MYBATIS = PRE_BE + "mybatis"; // mybatis相关

    String MYBATIS_INSERT = MYBATIS + ".insert"; // mybatis-insert相关

    String FILE_TYPE = PRE_BE + "file-type"; // 文件类型相关

    String NORMAL = PRE_BE + "normal"; // 没有指定 loggerName的日志，即：不以 be开头的 loggerName

    String USER = PRE_BE + "user"; // 用户相关

    String USER_INFO = PRE_BE + "user-info"; // 用户信息相关

    String REQUEST = PRE_BE + "request"; // 请求相关

    String SOCKET = PRE_BE + "socket"; // socket相关

    String NETTY_WEB_SOCKET = PRE_BE + "netty-web-socket"; // netty-web-socket相关

    String PAY = PRE_BE + "pay"; // 支付相关

    String THIRD_APP_WX = PRE_BE + "third-app-wx"; // 三方应用-微信相关

    String THIRD_APP_OFFICIAL_MENU = PRE_BE + "third-app-official-menu"; // 三方应用-公众号-菜单相关

    String THIRD_APP_WX_OFFICIAL = PRE_BE + "third-app-wx-official"; // 三方应用-微信公众号相关

    String THIRD_APP_WX_WORK = PRE_BE + "third-app-wx-work"; // 三方应用-企业微信相关

    String FFMPEG = PRE_BE + "ffmpeg"; // ffmpeg相关

}
