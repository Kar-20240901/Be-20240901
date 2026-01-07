package com.kar20240901.be.base.web.service.im.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240901.be.base.web.mapper.im.BaseImSessionMapper;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.domain.im.BaseImSessionDO;
import com.kar20240901.be.base.web.model.interfaces.im.IBaseImType;
import com.kar20240901.be.base.web.service.im.BaseImSessionService;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BaseImSessionServiceImpl extends ServiceImpl<BaseImSessionMapper, BaseImSessionDO>
    implements BaseImSessionService {

    private static ConcurrentHashMap<Long, Long> BASE_IM_SESSION_MAP = MapUtil.newConcurrentHashMap();

    /**
     * 添加一个：待更新的数据
     */
    public static void put(Long sessionId, Long lastReceiveTs) {

        BASE_IM_SESSION_MAP.put(sessionId, lastReceiveTs);

    }

    /**
     * 定时任务，保存数据
     */
    @PreDestroy
    @Scheduled(fixedDelay = 5000)
    public void scheduledSava() {

        ConcurrentHashMap<Long, Long> tempBaseImSessionMap;

        synchronized (BASE_IM_SESSION_MAP) {

            if (CollUtil.isEmpty(BASE_IM_SESSION_MAP)) {
                return;
            }

            tempBaseImSessionMap = BASE_IM_SESSION_MAP;
            BASE_IM_SESSION_MAP = MapUtil.newConcurrentHashMap();

        }

        // 目的：防止还有程序往：tempList，里面添加数据，所以这里等待一会
        MyThreadUtil.schedule(() -> {

            List<BaseImSessionDO> list = new ArrayList<>();

            for (Entry<Long, Long> item : tempBaseImSessionMap.entrySet()) {

                BaseImSessionDO baseImSessionDO = new BaseImSessionDO();

                baseImSessionDO.setId(item.getKey());

                baseImSessionDO.setLastReceiveTs(item.getValue());

                list.add(baseImSessionDO);

            }

            updateBatchById(list);

        }, DateUtil.offsetMillisecond(new Date(), 1500));

    }

    /**
     * 新增会话
     */
    @Override
    public void addSession(Long sessionId, Long sourceApplyId, IBaseImType iBaseImType) {

        Assert.notNull(sessionId);

        Assert.notNull(sourceApplyId);

        Assert.notNull(iBaseImType);

        BaseImSessionDO baseImSessionDO = new BaseImSessionDO();

        baseImSessionDO.setId(sessionId);

        baseImSessionDO.setSourceApplyId(sourceApplyId);

        baseImSessionDO.setSourceApplyType(iBaseImType.getCode());

        baseImSessionDO.setLastReceiveTs(TempConstant.NEGATIVE_ONE);

        save(baseImSessionDO);

    }

}
