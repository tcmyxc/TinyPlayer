package com.tcmyxc.fragment;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/2 15:44
 * @description : 关于软件Fragment
 */
public class AboutFragment extends BaseFragment{

    private TextView textView;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView() {
        textView = bindViewId(R.id.tv_app_des);
        textView.setAutoLinkMask(Linkify.ALL);// 文字中有链接可点
        textView.setMovementMethod(LinkMovementMethod.getInstance());// 文字可滚动
    }

    @Override
    protected void initData() {

    }
}