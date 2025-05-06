package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomReplayMapper;
import com.kar20240901.be.base.web.model.annotation.base.MyTransactional;
import com.kar20240901.be.base.web.model.constant.base.TempFileTempPathConstant;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomDataDO;
import com.kar20240901.be.base.web.model.domain.live.BaseLiveRoomReplayDO;
import com.kar20240901.be.base.web.service.live.BaseLiveRoomReplayService;
import com.kar20240901.be.base.web.util.file.BaseFileUtil;
import com.kar20240901.be.base.web.util.live.FfmpegUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ws.schild.jave.MultimediaObject;

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

        Date date = new Date();

        DateTime beginTime = DateUtil.offsetSecond(date, -18);

        DateTime endTime = DateUtil.offsetSecond(date, -9);

        List<BaseLiveRoomDataDO> list = ChainWrappers.lambdaQueryChain(baseLiveRoomDataMapper)
            .ge(BaseLiveRoomDataDO::getCreateTs, beginTime.getTime())
            .le(BaseLiveRoomDataDO::getCreateTs, endTime.getTime()).orderByAsc(BaseLiveRoomDataDO::getCreateTs).list();

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

        List<BaseLiveRoomReplayDO> insertList = new ArrayList<>();

        for (Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item : map.entrySet()) {

            for (Entry<Long, List<BaseLiveRoomDataDO>> subItem : item.getValue().entrySet()) {

                // 处理：生成文件
                handleGenerateReplay(item, subItem, date, insertList);

            }

        }

        ChainWrappers.lambdaUpdateChain(baseLiveRoomDataMapper).in(BaseLiveRoomDataDO::getId, deleteIdList).remove();

        saveBatch(insertList);

    }

    /**
     * 处理：生成文件
     */
    private static void handleGenerateReplay(Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item,
        Entry<Long, List<BaseLiveRoomDataDO>> subItem, Date date, List<BaseLiveRoomReplayDO> insertList) {

        List<File> deleteFileList = new ArrayList<>();

        try {

            // 执行
            doHandleGenerateReplay(item, subItem, date, insertList, deleteFileList);

        } finally {

            for (File file : deleteFileList) {

                FileUtil.del(file); // 删除文件

            }

        }

    }

    /**
     * 执行
     */
    @SneakyThrows
    private static void doHandleGenerateReplay(Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item,
        Entry<Long, List<BaseLiveRoomDataDO>> subItem, Date date, List<BaseLiveRoomReplayDO> insertList,
        List<File> deleteFileList) {

        String simpleUuid = IdUtil.simpleUUID();

        int index = 0;

        StrBuilder strBuilder = StrBuilder.create();

        for (BaseLiveRoomDataDO baseLiveRoomDataDO : subItem.getValue()) {

            byte[] data = baseLiveRoomDataDO.getData();

            String fileName = simpleUuid + "." + index;

            File file = FileUtil.touch(TempFileTempPathConstant.FFMPEG_TEMP_PATH + fileName);

            deleteFileList.add(file);

            FileUtil.writeBytes(data, file);

            strBuilder.append("file '").append(file.getPath()).append("'\n");

            index = index + 1;

        }

        File inputTxtFile = FileUtil.touch(TempFileTempPathConstant.FFMPEG_TEMP_PATH + simpleUuid + ".txt");

        deleteFileList.add(inputTxtFile);

        FileUtil.writeUtf8String(strBuilder.toString(), inputTxtFile);

        File tsFile = FileUtil.touch(TempFileTempPathConstant.FFMPEG_TEMP_PATH + simpleUuid + ".ts");

        deleteFileList.add(tsFile);

        FfmpegUtil.handleCmd(
            StrUtil.format(" -f concat -safe 0 -i {} -c copy {}", inputTxtFile.getPath(), tsFile.getPath()));

        // 上传文件
        Long fileId = BaseFileUtil.getTempFileId("", subItem.getKey(), "", null, tsFile);

        // 获取：ts文件视频总时长
        MultimediaObject multimediaObject = new MultimediaObject(tsFile);

        long duration = multimediaObject.getInfo().getDuration();

        BaseLiveRoomReplayDO baseLiveRoomReplayDO = new BaseLiveRoomReplayDO();

        baseLiveRoomReplayDO.setRoomId(item.getKey());
        baseLiveRoomReplayDO.setCreateTime(date);
        baseLiveRoomReplayDO.setFileId(fileId);
        baseLiveRoomReplayDO.setMs((int)duration);
        baseLiveRoomReplayDO.setBelongId(subItem.getKey());

        insertList.add(baseLiveRoomReplayDO);

    }

}
