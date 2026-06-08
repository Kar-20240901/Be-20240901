package com.kar20240901.be.base.web.service.server.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.kar20240901.be.base.web.model.constant.base.TempConstant;
import com.kar20240901.be.base.web.model.vo.server.BaseServerWorkInfoVO;
import com.kar20240901.be.base.web.service.server.BaseServerService;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

@Service
public class BaseServerServiceImpl implements BaseServerService {

    private static BaseServerWorkInfoVO baseServerWorkInfoVO;

    @PostConstruct
    public void postConstruct() {

        baseServerWorkInfoVO = getBaseServerWorkInfoVO();

    }

    @Scheduled(fixedDelay = TempConstant.MINUTE_3_EXPIRE_TIME)
    public void scheduledGetWorkInfo() {

        baseServerWorkInfoVO = getBaseServerWorkInfoVO();

    }

    /**
     * 服务器运行情况
     */
    @Override
    public BaseServerWorkInfoVO workInfo() {

        return baseServerWorkInfoVO;

    }

    /**
     * 获取：BaseServerWorkInfoVO对象
     */
    private static BaseServerWorkInfoVO getBaseServerWorkInfoVO() {

        BaseServerWorkInfoVO baseServerWorkInfoVO = new BaseServerWorkInfoVO();

        // 服务器内存信息
        GlobalMemory globalMemory = OshiUtil.getMemory();

        long memoryTotal = globalMemory.getTotal();

        long memoryAvailable = globalMemory.getAvailable();

        double memoryDiv = NumberUtil.div(memoryTotal - memoryAvailable, memoryTotal, 4);

        String memoryUsage = NumberUtil.decimalFormat("#.##%", memoryDiv);

        baseServerWorkInfoVO.setMemoryUsage(memoryUsage);

        // cpu信息
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();

        baseServerWorkInfoVO.setCpuUsage(cpuInfo.getUsed() + "%");

        // 磁盘信息
        long diskTotal = 0L;
        long diskUsable = 0L;

        OperatingSystem os = OshiUtil.getOs();

        List<OSFileStore> fileStoreList = os.getFileSystem().getFileStores();

        for (OSFileStore item : fileStoreList) {

            diskTotal += item.getTotalSpace();

            diskUsable += item.getUsableSpace();

        }

        double diskDiv = NumberUtil.div(diskTotal - diskUsable, diskTotal, 4);

        String diskUsage = NumberUtil.decimalFormat("#.##%", diskDiv);

        baseServerWorkInfoVO.setDiskUsage(diskUsage);

        return baseServerWorkInfoVO;

    }

}
