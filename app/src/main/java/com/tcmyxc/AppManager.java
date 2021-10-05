package com.tcmyxc;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 18:27
 * @description : todo
 */
public class AppManager extends Application {

    private static Gson gson;
    private static OkHttpClient httpClient;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        httpClient = new OkHttpClient();
        context = this;
    }

    public static Gson getGson() {
        return gson;
    }

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static Context getContext() {
        return context;
    }

    public static Resources getResource(){
        return context.getResources();
    }

    // 判断网络是否可用
    public static boolean isNetWorkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
