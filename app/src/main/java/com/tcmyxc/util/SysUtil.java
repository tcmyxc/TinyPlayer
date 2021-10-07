package com.tcmyxc.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.provider.Settings;

/**
 * @author : 徐文祥
 * @date : 2021/10/7 20:18
 * @description : 系统信息工具类
 */
public class SysUtil {

    // 获取亮度
    public static int getBrightness(Context context) {
        return Settings.System.getInt(context.getContentResolver(),"screen_brightness", -1);
    }

    // 设置亮度
    public static void setBrightness(Context context, int param) {
        Settings.System.putInt(context.getContentResolver(), "screen_brightness", param);
    }

    // 获取亮度的sharedPreferences文件
    public static int getDefaultBrightness(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("shared_preferences_light", -1);
    }
}
