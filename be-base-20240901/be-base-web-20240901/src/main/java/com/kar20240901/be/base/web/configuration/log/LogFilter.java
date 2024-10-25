package com.kar20240901.be.base.web.configuration.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.properties.log.LogProperties;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

@Slf4j
public class LogFilter extends Filter<ILoggingEvent> {

    public static LogProperties logProperties;

    static {

        MyTryUtil.tryCatch(LogFilter::initLogProperties);

    }

    private static void initLogProperties() {

        File file = FileUtil.touch("/home/conf/log.yml");

        handle(file);

        WatchMonitor.createAll(file, new Watcher() {

            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {

                handle(file);

            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {

                handle(file);

            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {

                handle(file);

            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {

                handle(file);

            }

        }).start();

    }

    private static void handle(File file) {

        String str = FileUtil.readUtf8String(file);

        logProperties = new Yaml().loadAs(str, LogProperties.class);

        log.info("logProperties：{}", JSONUtil.toJsonStr(logProperties));

    }

    /**
     * 备注：打印日志会影响 tps：2600 -> 2000
     */
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {

        if (logProperties != null && CollUtil.isNotEmpty(logProperties.getLogTopicSet())) {

            if (logProperties.getLogTopicSet().contains(iLoggingEvent.getLoggerName())) {

                return FilterReply.NEUTRAL; // 打印

            }

            if (logProperties.getLogTopicSet().contains(LogTopicConstant.NORMAL) && !iLoggingEvent.getLoggerName()
                .startsWith(LogTopicConstant.PRE_BE)) {

                if (logProperties.getNotLogTopicSet().contains(iLoggingEvent.getLoggerName())) {

                    return FilterReply.DENY; // 不打印

                }

                return FilterReply.NEUTRAL; // 打印

            }

            return FilterReply.DENY; // 不打印

        }

        if (iLoggingEvent.getLoggerName().startsWith(LogTopicConstant.PRE_BE)) {

            return FilterReply.DENY; // 不打印

        }

        return FilterReply.NEUTRAL; // 中立（一般都会打印）

    }

}
