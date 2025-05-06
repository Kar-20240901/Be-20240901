package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomReplayMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDataDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomReplayDO;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomReplayService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BaseLiveRoomReplayServiceImpl extends ServiceImpl<BaseLiveRoomReplayMapper, BaseLiveRoomReplayDO>
    implements BaseLiveRoomReplayService {

    @Resource
    BaseLiveRoomDataMapper baseLiveRoomDataMapper;

    /**
     * 定时任务，生成回放
     */
    @PreDestroy
    @Scheduled(fixedDelay = 8000)
    @MyTransactional
    public void scheduledGenerateReplay() {

        List<BaseLiveRoomDataDO> list =
            ChainWrappers.lambdaQueryChain(baseLiveRoomDataMapper).orderByAsc(BaseLiveRoomDataDO::getCreateTs).list();

        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 要删除的 id集合
        List<Long> deleteIdList = new ArrayList<>();

        Map<Long, Map<Long, List<BaseLiveRoomDataDO>>> map = MapUtil.newHashMap();

        for (BaseLiveRoomDataDO item : list) {

            deleteIdList.add(item.getId());

            Map<Long, List<BaseLiveRoomDataDO>> subMap =
                map.computeIfAbsent(item.getRoomId(), k -> MapUtil.newHashMap());

            List<BaseLiveRoomDataDO> baseLiveRoomDataDoList =
                subMap.computeIfAbsent(item.getCreateId(), k -> CollUtil.newArrayList());

            baseLiveRoomDataDoList.add(item);

        }

        Date date = new Date();

        List<BaseLiveRoomReplayDO> insertList = new ArrayList<>();

        for (Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item : map.entrySet()) {

            for (Entry<Long, List<BaseLiveRoomDataDO>> subItem : item.getValue().entrySet()) {

                List<Byte> byteList = new ArrayList<>();

                for (BaseLiveRoomDataDO baseLiveRoomDataDO : subItem.getValue()) {

                    CollUtil.addAll(byteList, baseLiveRoomDataDO.getData());

                }

                // 组装成 ts文件

                // 获取：ts文件视频总时长
                Integer ms = null;

                // 上传文件
                Long fileId = null;

                BaseLiveRoomReplayDO baseLiveRoomReplayDO = new BaseLiveRoomReplayDO();

                baseLiveRoomReplayDO.setRoomId(item.getKey());
                baseLiveRoomReplayDO.setCreateTime(date);
                baseLiveRoomReplayDO.setFileId(fileId);
                baseLiveRoomReplayDO.setMs(ms);
                baseLiveRoomReplayDO.setBelongId(subItem.getKey());

                insertList.add(baseLiveRoomReplayDO);

            }

        }

        ChainWrappers.lambdaUpdateChain(baseLiveRoomDataMapper).in(BaseLiveRoomDataDO::getId, deleteIdList).remove();

        saveBatch(insertList);

    }

}
