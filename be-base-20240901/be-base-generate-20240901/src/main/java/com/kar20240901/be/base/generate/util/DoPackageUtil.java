package com.kar20240901.be.base.generate.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Supplier;
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

    private String feRemotePath = "/mydata/fe/fe-20240901/html";

    private String feZipRemotePath = "/mydata/fe/fe-20240901/html-zip";

    private String feRemoteUnzipTempCmd = "unzip -o /mydata/fe/fe-20240901/html-zip/{} -d " + feRemotePath;

    /**
     * 备注：最后不要加 /
     */
    private String springRemotePath = "/mydata/springboot";

    private String springRemoteStopCmd = "docker stop be-base-web-node-1";

    private String springRemoteRenameTempCmd =
        "mv /mydata/springboot/be-base-web-20240901-2024.9.1.jar /mydata/springboot/be-base-web-20240901-2024.9.1.jar.bak{}";

    private String springRemoteMergeCmd =
        "cat /mydata/springboot/be-base-web-20240901-2024.9.1.jar.part.* > /mydata/springboot/be-base-web-20240901-2024.9.1.jar";

    private String springRemoteRemovePartFileCmd = "rm /mydata/springboot/be-base-web-20240901-2024.9.1.jar.part.*";

    private String springRemoteRestartCmd = "docker restart be-base-web-node-1";

    private String projectPath = System.getProperty("user.dir"); // 例如：D:\GitHub\Be-20240901

    private String feName = "Fe-20240901";

    private String beName = "Be-20240901/be-base-20240901";

    private String beJarName = "be-base-web-20240901-2024.9.1.jar";

    private String beJarPartName = "be-base-web-20240901-2024.9.1.jar.part.{}";

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

        List<Session> sessionList = new ArrayList<>();

        Supplier<Session> sessionSupplier = () -> {

            Session session = JschUtil.getSession(getHost(), 22, getUser(), getPrivateKeyPath(), null);

            sessionList.add(session);

            return session;

        };

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(threadCount);

        Date date = new Date();

        if (number == 1 || number == 2) {

            new Thread(() -> {

                doBePackage(getProjectPath(), sessionSupplier, countDownLatch, date);

            }).start();

        }

        if (number == 1 || number == 3) {

            new Thread(() -> {

                doFePackage(getProjectPath(), sessionSupplier, countDownLatch, date);

            }).start();

        }

        countDownLatch.await();

        for (Session item : sessionList) {

            JschUtil.close(item);

        }

        System.exit(0);

    }

    /**
     * 后端打包
     */
    public void doBePackage(String projectPath, Supplier<Session> sessionSupplier, CountDownLatch countDownLatch,
        Date date) {

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

            timeNumber = System.currentTimeMillis();

            // 拆分文件
            List<File> partFileList = splitFile(file.getPath(), 10 * 1024 * 1024);

            CountDownLatch subCountDownLatch = ThreadUtil.newCountDownLatch(partFileList.size());

            for (File item : partFileList) {

                new Thread(() -> {

                    try {

                        Session session = sessionSupplier.get();

                        Sftp sftp = JschUtil.createSftp(session);

                        sftp.put(item.getPath(), getSpringRemotePath());

                        JschUtil.close(sftp.getClient());

                    } finally {

                        subCountDownLatch.countDown();

                    }

                }).start();

            }

            subCountDownLatch.await();

            timeNumber = System.currentTimeMillis() - timeNumber;
            timeStr = DateUtil.formatBetween(timeNumber);

            log.info("后端打包上传 ↑ 耗时：" + timeStr);

            log.info("启动后端 ↓");

            timeNumber = System.currentTimeMillis();

            Consumer<Session> beRemoteRestartConsumer = getBeRemoteRestartConsumer();

            Session session = sessionSupplier.get();

            if (beRemoteRestartConsumer != null) {

                // 执行：重启之前的操作
                beRemoteRestartConsumer.accept(session);

            }

            // 先停止，再合并文件
            JschUtil.exec(session, getSpringRemoteStopCmd(), CharsetUtil.CHARSET_UTF_8);

            // 修改旧文件名
            JschUtil.exec(session, StrUtil.format(getSpringRemoteRenameTempCmd(), date.getTime()),
                CharsetUtil.CHARSET_UTF_8);

            // 合并文件
            JschUtil.exec(session, getSpringRemoteMergeCmd(), CharsetUtil.CHARSET_UTF_8);

            // 移除分片文件
            JschUtil.exec(session, getSpringRemoteRemovePartFileCmd(), CharsetUtil.CHARSET_UTF_8);

            // 重启服务
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

        }

    }

    /**
     * 拆分文件
     */
    public static List<File> splitFile(String filePath, int chunkSize) {

        File sourceFile = FileUtil.file(filePath);

        if (!sourceFile.exists()) {
            throw new RuntimeException("源文件不存在！");
        }

        long fileLength = sourceFile.length();

        int partCount = (int)(fileLength / chunkSize);

        if (fileLength % chunkSize != 0) {
            partCount++;
        }

        List<File> partFileList = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(sourceFile, "r")) {

            for (int i = 0; i < partCount; i++) {

                byte[] buffer = new byte[chunkSize];

                int readSize = raf.read(buffer);

                if (readSize == -1) {
                    break;
                }

                String partFileName = filePath + ".part." + i;

                File partFile = FileUtil.file(partFileName);

                FileUtil.writeBytes(buffer, partFile);

                partFileList.add(partFile);

            }

        } catch (IOException e) {

            throw new RuntimeException("拆分文件时出错", e);

        }

        return partFileList;

    }

    /**
     * 前端打包
     */
    public void doFePackage(String projectPath, Supplier<Session> sessionSupplier, CountDownLatch countDownLatch,
        Date date) {

        Session session = sessionSupplier.get();

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

            // 移除该文件，目的：不覆盖服务器上的文件
            FileUtil.del(file.getPath() + "/" + configFileName);

            // 压缩文件
            File zipFileTemp = ZipUtil.zip(file);

            File zipFile = FileUtil.newFile(file.getPath() + "/" + file.getName() + "." + date.getTime() + ".zip");

            FileUtil.move(zipFileTemp, zipFile, true);

            // 上传压缩文件
            sftp.put(zipFile.getPath(), getFeZipRemotePath());

            List<String> lsList = sftp.ls(getFeRemotePath());

            for (String item : lsList) {

                if (configFileName.equals(item)) {
                    continue; // 不做处理
                }

                String fullFileName = getFeRemotePath() + "/" + item;

                if (sftp.isDir(fullFileName)) {

                    sftp.delDir(fullFileName); // 删除，目录

                } else {

                    sftp.delFile(fullFileName); // 删除：文件

                }

            }

            // 解压压缩文件
            JschUtil.exec(session, StrUtil.format(getFeRemoteUnzipTempCmd(), zipFile.getName()),
                CharsetUtil.CHARSET_UTF_8);

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
