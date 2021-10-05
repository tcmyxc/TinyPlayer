package com.tcmyxc.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : 徐文祥
 * @date : 2021/10/2 15:35
 * @description : Fragment基类
 */
public abstract class BaseFragment extends Fragment {

    private View contentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = getActivity().getLayoutInflater().inflate(getLayoutId(), container, false);
        initView();
        initData();
        return contentView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId) {
        return (T) contentView.findViewById(resId);
    }

}
