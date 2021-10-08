package com.tcmyxc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.preference.PreferenceManager;

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
    public static void setBrightness(Context context, int brightness) {
        Settings.System.putInt(context.getContentResolver(), "screen_brightness", brightness);
        // 再存一份备份
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt("shared_preferences_light", brightness);
        editor.commit();
    }

    // 获取亮度的sharedPreferences文件
    public static int getDefaultBrightness(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("shared_preferences_light", -1);
    }
}
