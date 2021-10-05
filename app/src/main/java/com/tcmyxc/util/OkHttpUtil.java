package com.tcmyxc.util;

import com.tcmyxc.AppManager;

import okhttp3.Callback;
import okhttp3.Request;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 18:45
 * @description : todo
 */
public class OkHttpUtil {

    private static final String REQUEST_TAG = "okHttp";

    public static Request buildRequest(String url) {
        Request request = null;
        if (AppManager.isNetWorkAvailable()) {
            request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
        }
        return request;
    }

    public static void excute(String url, Callback callback) {
        Request request = buildRequest(url);
        excute(request, callback);
    }

    public static void excute(Request request, Callback callback) {
        AppManager.getHttpClient()
                .newCall(request)
                .enqueue(callback);
    }
}
