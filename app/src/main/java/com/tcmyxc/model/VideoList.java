package com.tcmyxc.model;

import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.util.LOG;

import java.util.ArrayList;

/**
 * @author : 徐文祥
 * @date : 2021/10/6 14:02
 * @description : VideoList
 */
public class VideoList extends ArrayList<Video> {

    private static final String TAG = VideoList.class.getName();

    public void debug() {
        for(Video video : this){
            LOG.i(TAG + " " + video.toString());
        }
    }
}
