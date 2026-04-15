package com.kar20240901.be.base.generate.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.cvm.v20170312.models.StartInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.StartInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.StopInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.StopInstancesResponse;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class MangeHostUtil {

    private static final String SECRET_ID = "";

    private static final String SECRET_KEY = "";

    private static final String REGION = "";

    private static final String[] INSTANCE_ID_ARR = new String[] {""};

    private static final String LOCAL_HOST_DOMAIN = "karopendev.top";

    public static void main(String[] args) {

        exec();

    }

    /**
     * 执行
     */
    public static void exec() {

        String nextLine = ApiTestHelper.getStrFromScanner("请输入：1 启动 2 停止（默认） 3 获取IP");

        int number = Convert.toInt(nextLine, 2);

        if (number == 1) {

            startTencentCloud(SECRET_ID, SECRET_KEY, REGION, INSTANCE_ID_ARR);

            ThreadUtil.sleep(20000);

            // 获取IP地址
            doGetPublicIpAddress();

        } else if (number == 3) {

            // 获取IP地址
            doGetPublicIpAddress();

        } else {

            stopTencentCloud(SECRET_ID, SECRET_KEY, REGION, INSTANCE_ID_ARR);

        }

    }

    /**
     * 获取IP地址
     */
    private static void doGetPublicIpAddress() {

        for (int i = 0; i < 10; i++) {

            log.info("正在获取IP地址，次数：{}", (i + 1));

            String publicIpAddress = getPublicIpAddress(SECRET_ID, SECRET_KEY, REGION, INSTANCE_ID_ARR);

            if (StrUtil.isNotBlank(publicIpAddress)) {

                log.info("IP地址是：{}", publicIpAddress);

                updateLocalHostFile(publicIpAddress, LOCAL_HOST_DOMAIN);

                return;

            }

            ThreadUtil.sleep(10000);

        }

    }

    /**
     * 修改本地HOST文件的内容
     */
    public static void updateLocalHostFile(String ip, String domain) {

        File hostsFile = FileUtil.newFile("C:\\Windows\\System32\\drivers\\etc\\hosts");

        List<String> lineList = FileUtil.readUtf8Lines(hostsFile);

        String newHostLine = ip + " " + domain;

        boolean updatedFlag = false;

        int realIndex = -1;

        for (int i = 0; i < lineList.size(); i++) {

            String line = lineList.get(i);

            if (StrUtil.isBlank(line) || line.startsWith("#")) {
                continue;
            }

            if (realIndex < 0) {
                realIndex = i;
            }

            // 匹配域名（忽略空格）
            if (line.contains(domain)) {

                lineList.set(i, newHostLine);

                updatedFlag = true;

                break;

            }

        }

        if (!updatedFlag) {

            // 添加到最前面
            lineList.add(realIndex, newHostLine);

        }

        // 写到文件里
        FileUtil.writeUtf8Lines(lineList, hostsFile);

        log.info("已成功写入hosts文件");

    }

    /**
     * 文档地址：https://console.cloud.tencent.com/api/explorer?Product=cvm&Version=2017-03-12&Action=StartInstances
     */
    @SneakyThrows
    public static void startTencentCloud(String secretId, String secretKey, String region, String[] instanceIdArr) {

        Credential cred = new Credential(secretId, secretKey);

        CvmClient client = new CvmClient(cred, region);

        StartInstancesRequest req = new StartInstancesRequest();

        req.setInstanceIds(instanceIdArr);

        StartInstancesResponse resp = client.StartInstances(req);

        log.info("腾讯云启动服务器-响应内容：{}", AbstractModel.toJsonString(resp));

    }

    /**
     * 文档地址：https://console.cloud.tencent.com/api/explorer?Product=cvm&Version=2017-03-12&Action=DescribeInstances
     */
    @Nullable
    @SneakyThrows
    public static String getPublicIpAddress(String secretId, String secretKey, String region, String[] instanceIdArr) {

        Credential cred = new Credential(secretId, secretKey);

        CvmClient client = new CvmClient(cred, region);

        DescribeInstancesRequest req = new DescribeInstancesRequest();

        req.setInstanceIds(instanceIdArr);

        DescribeInstancesResponse resp = client.DescribeInstances(req);

        if (ArrayUtil.isEmpty(resp.getInstanceSet())) {
            return null;
        }

        for (Instance item : resp.getInstanceSet()) {

            if (ArrayUtil.isEmpty(item.getPublicIpAddresses())) {
                return null;
            }

            return item.getPublicIpAddresses()[0];

        }

        return null;

    }

    /**
     * 文档地址：https://console.cloud.tencent.com/api/explorer?Product=cvm&Version=2017-03-12&Action=StopInstances
     */
    @SneakyThrows
    public static void stopTencentCloud(String secretId, String secretKey, String region, String[] instanceIdArr) {

        Credential cred = new Credential(secretId, secretKey);

        CvmClient client = new CvmClient(cred, region);

        StopInstancesRequest req = new StopInstancesRequest();

        // KEEP_CHARGING：关机继续收费
        // STOP_CHARGING：关机停止收费
        req.setStoppedMode("STOP_CHARGING");

        req.setInstanceIds(instanceIdArr);

        StopInstancesResponse resp = client.StopInstances(req);

        log.info("腾讯云停止服务器-响应内容：{}", AbstractModel.toJsonString(resp));

    }

}
