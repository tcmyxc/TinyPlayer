package com.tcmyxc.listener;

import com.tcmyxc.model.Album;
import com.tcmyxc.model.ErrorInfo;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 17:50
 * @description : AlbumDetailListener
 */
public interface GetAlbumDetailListener {

    void onGetAlbumDetailSuccess(Album album);
    void onGetAlbumDetailFailed(ErrorInfo errorInfo);
}
