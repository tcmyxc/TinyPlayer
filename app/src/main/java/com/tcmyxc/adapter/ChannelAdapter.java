package com.tcmyxc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.model.Channel;
import com.tcmyxc.util.LOG;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 13:03
 * @description : ChannelAdapter
 */
public class ChannelAdapter extends BaseAdapter {

    private Context context;

    public ChannelAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Channel.MAX_COUNT;
    }

    @Override
    public Object getItem(int position) {
        // LOG.i(this.getClass().getName() + ".getItem: " + "position is " + position);
        return new Channel(position + 1, context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Channel channel = (Channel) getItem(position);
        if(convertView  == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.home_grid_item, null);
            // 绑定视图上的组件
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.iv_home_item_img);
            viewHolder.textView = convertView.findViewById(R.id.tv_home_item_text);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(channel.getChannelName());
        int channelId = channel.getChannelId();
        int imgResId = -1;
        // 根据频道显示icon
        switch (channelId){
            case Channel.SHOW:
                imgResId = R.drawable.ic_show;
                break;
            case Channel.MOVIE:
                imgResId = R.drawable.ic_movie;
                break;
            case Channel.COMIC:
                imgResId = R.drawable.ic_comic;
                break;
            case Channel.DOCUMENTARY:
                imgResId = R.drawable.ic_movie;
                break;
            case Channel.MUSIC:
                imgResId = R.drawable.ic_music;
                break;
            case Channel.VARIETY:
                imgResId = R.drawable.ic_variety;
                break;
            case Channel.LIVE:
                imgResId = R.drawable.ic_live;
                break;
            case Channel.FAVORITE:
                imgResId = R.drawable.ic_bookmark;
                break;
            default:
                break;
        }

        viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(imgResId));

        return  convertView;
    }

    class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
}
