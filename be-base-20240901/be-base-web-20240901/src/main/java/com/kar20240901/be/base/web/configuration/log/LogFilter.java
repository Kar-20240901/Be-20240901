package com.kar20240901.be.base.web.configuration.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.properties.log.BaseProperties;
import com.kar20240901.be.base.web.util.base.MyTryUtil;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

@Slf4j
public class LogFilter extends Filter<ILoggingEvent> {

    public static BaseProperties baseProperties = new BaseProperties();

    static {

        try {

            // 初始化 baseProperties
            initBaseProperties();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 初始化 baseProperties
     */
    @SneakyThrows
    private static void initBaseProperties() {

        // 获取：文件对象
        File file = getFile();

        // 处理
        handle(file, "初始化");

        WatchService watchService = FileSystems.getDefault().newWatchService();

        file.getParentFile().toPath()
            .register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.OVERFLOW);

        ThreadUtil.schedule(ThreadUtil.createScheduledExecutor(1), () -> {

            WatchKey key = watchService.poll();

            if (null == key) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {

                Kind<?> kind = event.kind();

                String name = kind.name();

                if (name.equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {

                    MyTryUtil.tryCatch(() -> handle(file, "创建"));

                } else if (name.equals(StandardWatchEventKinds.ENTRY_MODIFY.name())) {

                    MyTryUtil.tryCatch(() -> handle(file, "修改"));

                } else if (name.equals(StandardWatchEventKinds.ENTRY_DELETE.name())) {

                    MyTryUtil.tryCatch(() -> handle(file, "删除"));

                } else if (name.equals(StandardWatchEventKinds.OVERFLOW.name())) {

                    MyTryUtil.tryCatch(() -> handle(file, "覆盖"));

                }

                // 每次的到新的事件后，需要重置监听池
                key.reset();

            }

        }, 10000, 10000, true);

    }

    /**
     * 获取：文件对象
     */
    private static File getFile() {

        File file = FileUtil.newFile("/home/conf/base.yml");

        if (FileUtil.exist(file)) {

            return file;

        }

        String userDirStr = System.getProperty("user.dir");// 例如：D:\GitHub\Be-20240901

        File newFile = FileUtil.newFile(userDirStr + "/conf/base.yml");

        if (FileUtil.exist(newFile)) {

            file = newFile;

        }

        return file;

    }

    /**
     * 处理
     */
    private static synchronized void handle(File file, String type) {

        String str;

        if (file.exists()) {

            str = FileUtil.readUtf8String(file);

        } else {

            str = ResourceUtil.readUtf8Str("base.yml");

            FileUtil.touch(file);

            FileUtil.writeUtf8String(str, file);

        }

        BaseProperties basePropertiesTemp = new Yaml().loadAs(str, BaseProperties.class);

        BeanUtil.copyProperties(basePropertiesTemp, baseProperties);

        System.out.println(
            "文件路径：" + file.getPath() + "，类型：" + type + "，值：" + JSONUtil.toJsonStr(baseProperties));

        log.info("【{}】baseProperties：{}", type, JSONUtil.toJsonStr(baseProperties));

    }

    /**
     * 备注：打印日志会影响 tps：2600 -> 2000
     */
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {

        if (baseProperties != null && CollUtil.isNotEmpty(baseProperties.getLogTopicSet())) {

            if (baseProperties.getLogTopicSet().contains(iLoggingEvent.getLoggerName())) {

                return FilterReply.NEUTRAL; // 打印

            }

            if (baseProperties.getLogTopicSet().contains(LogTopicConstant.NORMAL) && !iLoggingEvent.getLoggerName()
                .startsWith(LogTopicConstant.PRE_BE)) {

                if (baseProperties.getNotLogTopicSet().contains(iLoggingEvent.getLoggerName())) {

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
