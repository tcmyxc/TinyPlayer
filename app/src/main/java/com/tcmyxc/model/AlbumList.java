package com.tcmyxc.model;

import com.tcmyxc.util.LOG;

import java.util.ArrayList;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 18:17
 * @description : todo
 */
public class AlbumList extends ArrayList<Album> {

    private static final String TAG = AlbumList.class.getName();

    public void debug() {
        for(Album album : this){
            LOG.i(TAG + " " + album.toString());
        }
    }
}
