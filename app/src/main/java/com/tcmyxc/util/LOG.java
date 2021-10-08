package com.tcmyxc.util;

import android.util.Log;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 8:36
 * @description : 日志工具类
 */
public class LOG {

    // 开关，调试时设置为true，如果正式发布，打日志会比较影响性能
    public static final boolean DEBUG = false;
    // TAG
    public static final String TAG = "TinyPlayer";

    // 等级  DIWE（参考log4j）
    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }
}

