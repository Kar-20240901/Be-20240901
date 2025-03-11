package com.kar20240901.be.base.generate.util;

import java.util.Scanner;

/**
 * api测试帮助类
 */
public class ApiTestHelper {

    /**
     * 计算花费的时间
     */
    public static long calcCostMs(long currentTs) {

        return System.currentTimeMillis() - currentTs;

    }

    /**
     * 从控制台的输入里面获取字符串
     */
    public static String getStrFromScanner(String tip) {

        System.out.println(tip);

        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();

    }

}
