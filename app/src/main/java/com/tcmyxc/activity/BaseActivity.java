package com.tcmyxc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/2 13:20
 * @description : Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;// 顶部bar

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId){
        return (T) findViewById(resId);
    }

    // 支持toolbar
    protected void setSupportActionBar(){
        toolbar = bindViewId(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    // 支持icon
    protected void setActionBarIcon(int resId){
        if(toolbar != null){
            toolbar.setNavigationIcon(resId);
        }
    }

    // 支持返回小箭头
    protected void setSupportArrowActionBar(boolean isSupport){
        getSupportActionBar().setDisplayHomeAsUpEnabled(isSupport);
    }

}
