package com.tcmyxc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : 徐文祥
 * @date : 2021/10/7 20:17
 * @description : 日期工具类
 */
public class DateUtil {

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        return str;
    }
}
