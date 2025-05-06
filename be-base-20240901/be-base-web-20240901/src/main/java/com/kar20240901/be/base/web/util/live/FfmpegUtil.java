package com.kar20240901.be.base.web.util.live;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.kar20240901.be.base.web.model.constant.base.TempFileTempPathConstant;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

@Component
@Slf4j(topic = LogTopicConstant.FFMPEG)
public class FfmpegUtil {

    /**
     * 执行
     */
    @SneakyThrows
    public static void handleCmd(String cmd) {

        DefaultFFMPEGLocator defaultFfmpegLocator = new DefaultFFMPEGLocator();

        String executablePath = defaultFfmpegLocator.getExecutablePath();

        File parentFile = FileUtil.newFile(TempFileTempPathConstant.FFMPEG_TEMP_PATH);

        cmd = executablePath + cmd;

        log.info("ffmpeg 本次执行的命令：{}", cmd);

        Process process = RuntimeUtil.exec(null, parentFile, cmd);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {

                log.info("ffmpeg 输出：{}", line);

            }

        }

        // 等待 ffmpeg命令执行完毕
        process.waitFor();

    }

}
