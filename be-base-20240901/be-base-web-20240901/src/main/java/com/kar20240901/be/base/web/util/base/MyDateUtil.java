package com.kar20240901.be.base.web.util.base;

import cn.hutool.core.date.DateUtil;
import java.util.Date;

public class MyDateUtil {

    /**
     * 通过日期，获取 Cron 表达式
     */
    public static String getCron(Date date) {
        return DateUtil.format(date, "ss mm HH dd MM ? yyyy");
    }

}
