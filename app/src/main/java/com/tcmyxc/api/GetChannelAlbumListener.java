package com.tcmyxc.api;

import com.tcmyxc.model.AlbumList;
import com.tcmyxc.model.ErrorInfo;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 17:50
 * @description : todo
 */
public interface GetChannelAlbumListener {

    void onGetChannelAlbumSuccess(AlbumList albumList);
    void onGetChannelAlbumFailed(ErrorInfo errorInfo);
}
