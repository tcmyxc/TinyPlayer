package com.tcmyxc.listener;

import com.tcmyxc.model.Album;
import com.tcmyxc.model.ErrorInfo;

/**
 * @author : 徐文祥
 * @date : 2021/10/11 21:01
 * @description : 获取首页轮播图的Listener
 */
public interface GetLoopViewPageDataListener {

    void onGetLoopViewPageDataSuccess(Album album);
    void onGetLoopViewPageDataFailed(ErrorInfo errorInfo);
}
