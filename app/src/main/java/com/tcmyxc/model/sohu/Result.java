package com.tcmyxc.model.sohu;

import com.google.gson.annotations.Expose;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 19:15
 * @description : 搜狐频道数据返回类
 */
public class Result {

    @Expose
    private long status;

    @Expose
    private String statusText;

    // 数据结果
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
