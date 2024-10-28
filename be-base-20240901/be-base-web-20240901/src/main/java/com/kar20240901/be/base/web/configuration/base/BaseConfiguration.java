package com.kar20240901.be.base.web.configuration.base;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HttpGlobalConfig;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan(basePackages = "com.kar20240901.be")
@MapperScan(basePackages = "com.kar20240901.be.**.mapper")
@EnableAsync
@EnableScheduling
@AutoConfigureOrder(value = Integer.MIN_VALUE) // 如果配置在；spring.factories 文件里，则可以通过该注解指定加载顺序
@Slf4j
public class BaseConfiguration implements ApplicationRunner {

    public static String applicationName; // 服务名
    public static Integer port; // 启动的端口
    public static String profilesActive; // 启动的环境

    public static final Long START_TIME = System.currentTimeMillis(); // 启动时间

    public static final String MAC_ADDRESS = NetUtil.getLocalMacAddress(); // mac地址

    public BaseConfiguration(@Value("${spring.application.name:applicationName}") String applicationName,
        @Value("${server.port:8080}") int port, @Value("${spring.profiles.active:prod}") String profilesActive) {

        BaseConfiguration.applicationName = applicationName;
        BaseConfiguration.port = port;
        BaseConfiguration.profilesActive = profilesActive;

        // 设置：http超时时间，默认：30分钟
        HttpGlobalConfig.setTimeout(30 * 60 * 1000);

    }

    /**
     * 获取：是否是正式环境
     */
    public static boolean prodFlag() {

        return "prod".equals(BaseConfiguration.profilesActive);

    }

    /**
     * 获取：是否是开发环境
     */
    public static boolean devFlag() {

        return "dev".equals(BaseConfiguration.profilesActive);

    }

    /**
     * 设置：@Async的线程池
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(value = TaskExecutor.class)
    public TaskExecutor myTaskExecutor() {

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置：核心线程数
        executor.setCorePoolSize(availableProcessors * 10);

        // 设置：最大线程数
        executor.setMaxPoolSize(availableProcessors * 100);

        // 设置：队列容量
        executor.setQueueCapacity(availableProcessors * 100);

        // 设置：核心线程之外的线程，在空闲多久之后会被销毁的时间，备注：默认情况下，核心线程不会被销毁
        executor.setKeepAliveSeconds(60);

        // 设置：线程名前缀
        executor.setThreadNamePrefix("myTaskExecutor-");

        // 设置：线程池通用属性
        setCommonExecutor(executor);

        return executor;

    }

    /**
     * 设置：@Scheduled 的线程池，备注：额外加了 @Async注解，会在 @Async的线程池里面执行
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(value = TaskScheduler.class)
    public TaskScheduler myTaskScheduler() {

        int availableProcessors = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // 设置：核心线程数，备注：默认情况下，核心线程不会被销毁
        scheduler.setPoolSize(availableProcessors * 10);

        // 设置：线程名前缀
        scheduler.setThreadNamePrefix("myTaskScheduler-");

        // 设置：线程池通用属性
        setCommonExecutor(scheduler);

        return scheduler;

    }

    /**
     * 设置：线程池通用属性
     */
    public void setCommonExecutor(ExecutorConfigurationSupport support) {

        // 设置：拒绝策略：由调用线程处理该任务
        support.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 设置：等待所有任务结束后再关闭线程池
        support.setWaitForTasksToCompleteOnShutdown(true);

        // 设置：最多等待多少秒
        support.setAwaitTerminationSeconds(60);

        // 执行初始化
        support.initialize();

    }

    @Override
    public void run(ApplicationArguments args) {

        log.info("服务已启动，耗时：{}毫秒，地址：http://localhost:{}",
            DateUtil.formatBetween(System.currentTimeMillis() - START_TIME), port);

    }

}
