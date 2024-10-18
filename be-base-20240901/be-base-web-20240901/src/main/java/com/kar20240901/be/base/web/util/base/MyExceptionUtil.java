package com.kar20240901.be.base.web.util.base;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyExceptionUtil {

    /**
     * 打印异常日志
     */
    public static void printError(Throwable e) {

        log.error("异常日志打印，userId：{}", MyUserUtil.getCurrentUserIdDefault(), e);

    }

}
