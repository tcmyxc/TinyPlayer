package com.tcmyxc.model.sohu;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 徐文祥
 * @date : 2021/10/5 22:19
 * @description : 视频数据返回结果（json字段中的二级结构data）
 */
public class VideoData {

    @Expose
    private int count;

    @Expose
    private List<Video> videos = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Video> getVideoList() {
        return videos;
    }

    public void setVideoList(List<Video> videoList) {
        this.videos = videoList;
    }
}
