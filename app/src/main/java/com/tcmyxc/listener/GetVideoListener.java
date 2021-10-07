package com.tcmyxc.listener;

import com.tcmyxc.model.Album;
import com.tcmyxc.model.ErrorInfo;
import com.tcmyxc.model.VideoList;
import com.tcmyxc.model.sohu.Video;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 17:50
 * @description : VideoListener
 */
public interface GetVideoListener {

    void onGetVideoSuccess(VideoList videoList);
    void onGetVideoFailed(ErrorInfo errorInfo);
}
