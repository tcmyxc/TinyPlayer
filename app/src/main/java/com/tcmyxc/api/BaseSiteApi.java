package com.tcmyxc.api;

import com.tcmyxc.model.Channel;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 17:42
 * @description : BaseSiteApi
 */
public abstract class BaseSiteApi {
    public abstract void onGetChannelAlbums(Channel channel, int pageNo,
                                            int pageSize, GetChannelAlbumListener listener);
}
