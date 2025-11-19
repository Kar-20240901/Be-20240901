package com.kar20240901.be.base.web.util.sql;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.sql.BaseSqlSlowDO;
import com.kar20240901.be.base.web.service.sql.BaseSqlSlowService;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MySqlUtil {

    @Resource
    BaseSqlSlowService baseSlowService;

    private static CopyOnWriteArrayList<BaseSqlSlowDO> BASE_SQL_SLOW_DO_LIST = new CopyOnWriteArrayList<>();

    /**
     * 添加一个：慢sql日志数据
     */
    public static void add(BaseSqlSlowDO baseSqlSlowDO) {

        BASE_SQL_SLOW_DO_LIST.add(baseSqlSlowDO);

    }

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        CopyOnWriteArrayList<BaseSqlSlowDO> tempBaseSqlSlowDoList;

        synchronized (BASE_SQL_SLOW_DO_LIST) {

            if (CollUtil.isEmpty(BASE_SQL_SLOW_DO_LIST)) {
                return;
            }

            tempBaseSqlSlowDoList = BASE_SQL_SLOW_DO_LIST;
            BASE_SQL_SLOW_DO_LIST = new CopyOnWriteArrayList<>();

        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            // 批量保存数据
            baseSlowService.saveBatch(tempBaseSqlSlowDoList);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 定时任务，删除数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = TempConstant.MINUTE_5_EXPIRE_TIME)
    public void scheduledDelete() {

        // 只保留 6个月的数据
        DateTime dateTime = DateUtil.offsetMonth(new Date(), -6);

        baseSlowService.lambdaUpdate().le(BaseSqlSlowDO::getCreateTime, dateTime).remove();

    }

}
