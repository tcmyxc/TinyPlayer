package com.tcmyxc.model;

import android.content.Context;

import com.tcmyxc.R;

import java.io.Serializable;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 13:04
 * @description : Channel类
 */
public class Channel implements Serializable {

    public static final int SHOW = 1;// 电视剧
    public static final int MOVIE = 2;// 电影
    public static final int COMIC = 3;// 动漫
    public static final int DOCUMENTARY = 4;// 纪录片
    public static final int MUSIC = 5;// 音乐
    public static final int VARIETY = 6;// 综艺
    public static final int LIVE = 7;// 直播
    public static final int FAVORITE = 8;// 收藏

    public static final int MAX_COUNT = 8;// 频道数

    private int channelId;
    private String channelName;
    private Context context;

    public Channel(int channelId, Context context) {
        this.channelId = channelId;
        this.context = context;

        switch (channelId) {
            case SHOW:
                channelName = context.getResources().getString(R.string.channel_series);
                break;
            case MOVIE:
                channelName = context.getResources().getString(R.string.channel_movie);
                break;
            case COMIC:
                channelName = context.getResources().getString(R.string.channel_comic);
                break;
            case DOCUMENTARY:
                channelName = context.getResources().getString(R.string.channel_documentary);
                break;
            case MUSIC:
                channelName = context.getResources().getString(R.string.channel_music);
                break;
            case VARIETY:
                channelName = context.getResources().getString(R.string.channel_variety);
                break;
            case LIVE:
                channelName = context.getResources().getString(R.string.channel_live);
                break;
            case FAVORITE:
                channelName = context.getResources().getString(R.string.channel_favorite);
                break;
            default:
                break;
        }
    }

    public int getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }
}
