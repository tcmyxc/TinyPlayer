package com.tcmyxc.listener;

import com.tcmyxc.model.ErrorInfo;
import com.tcmyxc.model.sohu.Video;

/**
 * @author : 徐文祥
 * @date : 2021/10/6 16:49
 * @description : GetVideoPlayUrlListener
 */
public interface GetVideoPlayUrlListener {

    // 超清、高清、标清
    void onGetSuperUrl(Video video, String url);
    void onGetHighUrl(Video video, String url);
    void onGetNormalUrl(Video video, String url);
    void onGetFailed(ErrorInfo info);
}
