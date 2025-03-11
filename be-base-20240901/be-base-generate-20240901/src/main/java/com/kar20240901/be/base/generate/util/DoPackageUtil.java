package com.kar20240901.be.base.generate.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.Session;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 打包工具类
 */
@Slf4j
@Data
public class DoPackageUtil {

    private String host = "karopendev.top";

    private String user = "root";

    private String privateKeyPath = "/home/key/key1.pem";

    private String viteRemotePath = "/mydata/vue/h5";

    /**
     * 备注：最后不要加 /
     */
    private String springRemotePath = "/mydata/springboot";

    private String springRemoteStopCmd = "docker stop be-base-web-node-1";

    private String springRemoteRestartCmd = "docker restart be-base-web-node-1";

    private String projectPath = System.getProperty("user.dir"); // 例如：D:\GitHub\Be-20240901

    private String feName = "Fe-20240901";

    private String beName = "Be-20240901/be-base-20240901";

    private String beJarName = "be-base-web-20240901-2024.9.1.jar";

    private String beStartFolderName = "be-base-web-20240901";

    private String beJarPath = getBeStartFolderName() + "/target/" + getBeJarName();

    private String npmBuildCmd = "pnpm build";

    /**
     * 后端，打包之前的操作
     */
    public Consumer<String> getBePackagePreConsumer() {
        return null;
    }

    /**
     * 后端，重启之前的操作
     */
    public Consumer<Session> getBeRemoteRestartConsumer() {
        return null;
    }

    public String getProjectPath() {

        if (projectPath.contains("\\Be-20240901")) {
            return projectPath.replace("\\Be-20240901", "");
        }

        return projectPath;

    }

    /**
     * 打包：前端和后端
     */
    @SneakyThrows
    public static void main(String[] args) {

        DoPackageUtil doPackageUtil = new DoPackageUtil();

        doPackageUtil.exec();

    }

    @SneakyThrows
    public void exec() {

        String nextLine = ApiTestHelper.getStrFromScanner("请输入：1 全部打包 2 后端打包 3 前端打包");

        int number = Convert.toInt(nextLine, 1);

        int threadCount;

        if (number == 2 || number == 3) {
            threadCount = 1;
        } else {
            threadCount = 2;
        }

        Session session = JschUtil.getSession(getHost(), 22, getUser(), getPrivateKeyPath(), null);

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(threadCount);

        if (number == 1 || number == 2) {

            new Thread(() -> {

                doBePackage(getProjectPath(), session, countDownLatch);

            }).start();

        }

        if (number == 1 || number == 3) {

            new Thread(() -> {

                doFePackage(getProjectPath(), session, countDownLatch);

            }).start();

        }

        countDownLatch.await();

        JschUtil.close(session);

        System.exit(0);

    }

    /**
     * 后端打包
     */
    public void doBePackage(String projectPath, Session session, CountDownLatch countDownLatch) {

        Sftp sftp = JschUtil.createSftp(session);

        try {

            log.info("后端打包 ↓");

            long beginTimeNumber = System.currentTimeMillis();

            long timeNumber = System.currentTimeMillis();

            projectPath = projectPath + "/" + getBeName();

            Consumer<String> bePackagePreConsumer = getBePackagePreConsumer();

            if (bePackagePreConsumer != null) {

                // 执行：打包之前的操作
                bePackagePreConsumer.accept(projectPath);

            }

            // 这里需要先清除一下，不然不会重新打包
            RuntimeUtil.execForStr("cmd", "/c", "cd " + projectPath + "/" + getBeStartFolderName() + " && mvn clean");

            RuntimeUtil.execForStr("cmd", "/c", "cd " + projectPath + "/" + getBeStartFolderName() + " && mvn package");

            timeNumber = System.currentTimeMillis() - timeNumber;
            String timeStr = DateUtil.formatBetween(timeNumber);

            log.info("后端打包 ↑ 耗时：" + timeStr);

            String jarPath = projectPath + "/" + getBeJarPath();

            File file = FileUtil.newFile(jarPath);

            log.info("后端打包上传 ↓ 大小：" + DataSizeUtil.format(FileUtil.size(file)));

            // 先停止，再上传文件
            JschUtil.exec(session, getSpringRemoteStopCmd(), CharsetUtil.CHARSET_UTF_8);

            timeNumber = System.currentTimeMillis();

            sftp.put(jarPath, getSpringRemotePath());

            timeNumber = System.currentTimeMillis() - timeNumber;
            timeStr = DateUtil.formatBetween(timeNumber);

            log.info("后端打包上传 ↑ 耗时：" + timeStr);

            log.info("启动后端 ↓");

            timeNumber = System.currentTimeMillis();

            Consumer<Session> beRemoteRestartConsumer = getBeRemoteRestartConsumer();

            if (beRemoteRestartConsumer != null) {

                // 执行：重启之前的操作
                beRemoteRestartConsumer.accept(session);

            }

            JschUtil.exec(session, getSpringRemoteRestartCmd(), CharsetUtil.CHARSET_UTF_8);

            timeNumber = System.currentTimeMillis() - timeNumber;
            timeStr = DateUtil.formatBetween(timeNumber);

            log.info("启动后端 ↑ 耗时：" + timeStr);

            beginTimeNumber = System.currentTimeMillis() - beginTimeNumber;
            timeStr = DateUtil.formatBetween(beginTimeNumber);

            log.info("后端相关操作执行完毕！==================== 总耗时：" + timeStr);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            countDownLatch.countDown();
            JschUtil.close(sftp.getClient());

        }

    }

    /**
     * 前端打包
     */
    public void doFePackage(String projectPath, Session session, CountDownLatch countDownLatch) {

        Sftp sftp = JschUtil.createSftp(session);

        try {

            log.info("前端打包 ↓");

            long beginTimeNumber = System.currentTimeMillis();

            long timeNumber = System.currentTimeMillis();

            projectPath = projectPath + "/" + getFeName();

            String viteBuildPath = projectPath + "/dist";

            FileUtil.del(viteBuildPath); // 先删除：原来打包的文件夹

            RuntimeUtil.execForStr("cmd", "/c", "cd " + projectPath + " && " + getNpmBuildCmd());

            timeNumber = System.currentTimeMillis() - timeNumber;
            String timeStr = DateUtil.formatBetween(timeNumber);

            log.info("前端打包 ↑ 耗时：" + timeStr);

            File file = FileUtil.mkdir(viteBuildPath); // 再创建文件夹
            long size = FileUtil.size(file);

            if (size == 0) {

                log.info("前端打包上传失败，文件大小为 0");
                return;

            }

            log.info("前端打包上传 ↓ 大小：" + DataSizeUtil.format(size));

            timeNumber = System.currentTimeMillis();

            String configFileName = "config.js";

            for (String item : sftp.ls(getViteRemotePath())) {

                if (configFileName.equals(item)) {
                    continue; // 不做处理
                }

                String fullFileName = getViteRemotePath() + "/" + item;

                if (sftp.isDir(fullFileName)) {

                    sftp.delDir(fullFileName); // 删除，目录

                } else {

                    sftp.delFile(fullFileName); // 删除：文件

                }

            }

            // 移除该文件，目的：不覆盖服务器上的文件
            FileUtil.del(file.getPath() + "/" + configFileName);

            sftp.syncUpload(file, getViteRemotePath());

            timeNumber = System.currentTimeMillis() - timeNumber;
            timeStr = DateUtil.formatBetween(timeNumber);

            log.info("前端打包上传 ↑ 耗时：" + timeStr);

            beginTimeNumber = System.currentTimeMillis() - beginTimeNumber;
            timeStr = DateUtil.formatBetween(beginTimeNumber);

            log.info("前端相关操作执行完毕！==================== 总耗时：" + timeStr);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            countDownLatch.countDown();
            JschUtil.close(sftp.getClient());

        }

    }

}
