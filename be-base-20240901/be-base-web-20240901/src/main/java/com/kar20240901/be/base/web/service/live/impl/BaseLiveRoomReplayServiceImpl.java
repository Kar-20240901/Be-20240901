package com.kar20240901.be.base.web.service.live.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomDataMapper;
import com.kar20240901.be.base.web.mapper.live.BaseLiveRoomReplayMapper;
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
import javax.annotation.Resource;
import lombok.SneakyThrows;
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
    //    @PreDestroy
    //    @Scheduled(fixedDelay = 9000)
    @DSTransactional
    public void scheduledGenerateReplay() {

        Date date = new Date();

        //        DateTime beginTime = DateUtil.offsetSecond(date, -18);
        //
        //        DateTime endTime = DateUtil.offsetSecond(date, -9);

        List<BaseLiveRoomDataDO> list =
            ChainWrappers.lambdaQueryChain(baseLiveRoomDataMapper).orderByAsc(BaseLiveRoomDataDO::getCreateTs).list();

        if (CollUtil.isEmpty(list)) {
            return;
        }

        // 要删除的 id集合
        List<Long> deleteIdList = new ArrayList<>();

        Map<Long, Map<Long, List<BaseLiveRoomDataDO>>> map = MapUtil.newHashMap();

        for (BaseLiveRoomDataDO item : list) {

            Map<Long, List<BaseLiveRoomDataDO>> subMap =
                map.computeIfAbsent(item.getRoomId(), k -> MapUtil.newHashMap());

            List<BaseLiveRoomDataDO> baseLiveRoomDataDoList =
                subMap.computeIfAbsent(item.getCreateId(), k -> CollUtil.newArrayList());

            baseLiveRoomDataDoList.add(item);

        }

        List<BaseLiveRoomReplayDO> insertList = new ArrayList<>();

        try {

            for (Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item : map.entrySet()) {

                for (Entry<Long, List<BaseLiveRoomDataDO>> subItem : item.getValue().entrySet()) {

                    if (subItem.getValue().size() < 10) {
                        continue;
                    }

                    // 处理：生成文件
                    handleGenerateReplay(item, subItem, date, insertList, deleteIdList);

                }

            }

        } finally {

            if (CollUtil.isNotEmpty(deleteIdList)) {

                ChainWrappers.lambdaUpdateChain(baseLiveRoomDataMapper).in(BaseLiveRoomDataDO::getId, deleteIdList)
                    .remove();

            }

            if (CollUtil.isNotEmpty(insertList)) {

                saveBatch(insertList);

            }

        }

    }

    /**
     * 处理：生成文件
     */
    private void handleGenerateReplay(Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item,
        Entry<Long, List<BaseLiveRoomDataDO>> subItem, Date date, List<BaseLiveRoomReplayDO> insertList,
        List<Long> deleteIdList) {

        List<File> deleteFileList = new ArrayList<>();

        try {

            // 执行
            doHandleGenerateReplay(item, subItem, date, insertList, deleteFileList, deleteIdList);

        } finally {

            for (File file : deleteFileList) {

                //                FileUtil.del(file); // 删除文件

            }

        }

    }

    /**
     * 执行
     */
    @SneakyThrows
    private void doHandleGenerateReplay(Entry<Long, Map<Long, List<BaseLiveRoomDataDO>>> item,
        Entry<Long, List<BaseLiveRoomDataDO>> subItem, Date date, List<BaseLiveRoomReplayDO> insertList,
        List<File> deleteFileList, List<Long> deleteIdList) {

        String simpleUuid = IdUtil.simpleUUID();

        StrBuilder strBuilder = StrBuilder.create();

        for (int i = 0; i < subItem.getValue().size(); i++) {

            BaseLiveRoomDataDO baseLiveRoomDataDO = subItem.getValue().get(i);

            byte[] data = baseLiveRoomDataDO.getData();

            String fileName = simpleUuid + "." + i + ".mp4";

            File file = FileUtil.touch(TempFileTempPathConstant.FFMPEG_TEMP_PATH + fileName);

            deleteFileList.add(file);

            FileUtil.writeBytes(data, file);

            strBuilder.append("file '").append(file.getAbsolutePath()).append("'\n");

            deleteIdList.add(baseLiveRoomDataDO.getId());

        }

        File inputTxtFile = FileUtil.touch(TempFileTempPathConstant.FFMPEG_TEMP_PATH + simpleUuid + ".txt");

        deleteFileList.add(inputTxtFile);

        FileUtil.writeUtf8String(strBuilder.toString(), inputTxtFile);

        File tsFile = FileUtil.touch(TempFileTempPathConstant.FFMPEG_TEMP_PATH + simpleUuid + ".ts");

        deleteFileList.add(tsFile);

        FfmpegUtil.handleCmd(StrUtil.format(" -y -f concat -safe 0 -i {} -c copy {}", inputTxtFile.getAbsolutePath(),
            tsFile.getAbsolutePath()));

        // 上传文件
        Long fileId = BaseFileUtil.getTempFileId("", subItem.getKey(), "", null, tsFile, false);

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
