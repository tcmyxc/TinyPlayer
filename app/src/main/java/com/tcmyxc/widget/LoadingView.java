package com.tcmyxc.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/6 14:51
 * @description : LoadingView
 */
public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.loading_view_layout ,this);
    }
}
