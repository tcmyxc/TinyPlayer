package com.tcmyxc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.tcmyxc.R;
import com.tcmyxc.activity.GuideActivity;
import com.tcmyxc.activity.HomeActivity;

public class SplashActivity extends Activity {

    private SharedPreferences sharedPreferences;

    public static final int GO_HOME = 1;
    public static final int GO_GUIDE = 2;
    public static final int ENTER_DURATION = 2000;// 2秒延时

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case GO_HOME:
                    startHomeActivity();
                    break;
                case GO_GUIDE:
                    startGuideActivity();
                    break;
                default:
                    break;
            }
        }
    };

    // 进入引导页
    private void startGuideActivity() {
        startActivity(new Intent(this, GuideActivity.class));
        finish();
    }

    // 进入主页
    private void startHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        init();

    }

    // 初始化
    private void init() {
        boolean isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
        // 如果第一次进入APP，则进入引导页，否则直接进入主页
        if(isFirstIn){
            handler.sendEmptyMessageDelayed(GO_GUIDE, ENTER_DURATION);
        }
        else {
            handler.sendEmptyMessageDelayed(GO_HOME, ENTER_DURATION);
        }
    }
}