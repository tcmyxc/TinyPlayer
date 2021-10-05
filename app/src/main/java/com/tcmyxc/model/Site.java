package com.tcmyxc.model;

import android.content.Context;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 15:23
 * @description : Site类
 */
public class Site {

    public static final int SOHU = 1;// 搜狐
    public static final int MAX_SITE = 2;

    private int siteId;
    private String siteName;

    // 数据模型不应该持有context
    // 同时也是为了提高加载速度
    public Site(int id) {
        siteId = id;
        switch (siteId) {
            case SOHU:
                siteName = "搜狐视频";
                break;
            default:
                break;
        }
    }

    public int getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }
}
