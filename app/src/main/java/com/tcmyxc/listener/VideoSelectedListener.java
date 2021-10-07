package com.tcmyxc.listener;

import com.tcmyxc.model.sohu.Video;

/**
 * @author : 徐文祥
 * @date : 2021/10/6 14:22
 * @description : VideoSelectedListener
 */
public interface VideoSelectedListener {
    void onVideoSelected(Video video, int position);
}
