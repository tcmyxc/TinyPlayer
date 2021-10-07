package com.tcmyxc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tcmyxc.R;
import com.tcmyxc.model.VideoList;
import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.util.LOG;

/**
 * @author : 徐文祥
 * @date : 2021/10/6 14:18
 * @description : VideoItemAdapter
 */
public class VideoItemAdapter extends BaseAdapter {

    private Context context;
    private int totalCount;
    private VideoSelectedListener listener;
    private VideoList videoList = new VideoList();
    private boolean isShowTitleContent;
    private boolean isFirst = true;

    public VideoItemAdapter(Context context, int totalCount, VideoSelectedListener listener) {
        this.context = context;
        this.totalCount = totalCount;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Video getItem(int position) {
        return videoList.size() > 0 ? videoList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Video video = getItem(position);// 当前video
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.video_item_layout, null);
            holder = new ViewHolder();
            holder.videoContainer = convertView.findViewById(R.id.video_container);
            holder.videoTitleBtn = convertView.findViewById(R.id.bt_video_title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(getIsShowTitleContent()){
            if(!TextUtils.isEmpty(video.getVideoName())){
                holder.videoTitleBtn.setText(video.getVideoName());
            }
            else {
                holder.videoTitleBtn.setText(String.valueOf(position + 1));
            }
        }
        else {
            holder.videoTitleBtn.setText(String.valueOf(position + 1));
        }
        // 首次进入页面，需要默认选中第一集
        if(isFirst){
            listener.onVideoSelected(video, 0);
            isFirst = false;
        }
        // 设置集数按钮点击事件
        holder.videoTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onVideoSelected(video, position);
            }
        });

        return convertView;
    }

    public void addVideo(Video video){
        videoList.add(video);
    }

    public void setIsShowTitleContent(boolean isShowTitleContent) {
        this.isShowTitleContent = isShowTitleContent;
    }

    public boolean getIsShowTitleContent() {
        return isShowTitleContent;
    }

    class ViewHolder {
        LinearLayout videoContainer;
        Button videoTitleBtn;
    }
}
