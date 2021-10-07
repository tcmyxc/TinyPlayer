package com.tcmyxc.model.sohu;

import com.google.gson.annotations.Expose;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 19:15
 * @description : 详情页面数据类
 */
public class DetailResult {

    @Expose
    private long status;

    @Expose
    private String statusText;

    // 详情页面
    @Expose
    private ResultAlbum data;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public ResultAlbum getData() {
        return data;
    }

    public void setData(ResultAlbum data) {
        this.data = data;
    }
}
