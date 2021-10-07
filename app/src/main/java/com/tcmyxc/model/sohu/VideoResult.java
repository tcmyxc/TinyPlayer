package com.tcmyxc.model.sohu;

import com.google.gson.annotations.Expose;

/**
 * @author : 徐文祥
 * @date : 2021/10/5 22:21
 * @description : 视频数据返回结果
 */
public class VideoResult {

    @Expose
    private long status;

    @Expose
    private String statusText;

    @Expose
    private VideoData data;

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

    public VideoData getData() {
        return data;
    }

    public void setData(VideoData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VideoResult{" +
                "status=" + status +
                ", statusText='" + statusText + '\'' +
                ", data=" + data +
                '}';
    }
}
