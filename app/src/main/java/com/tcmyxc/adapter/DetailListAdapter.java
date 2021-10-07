package com.tcmyxc.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.activity.AlbumDetailActivity;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.model.Channel;
import com.tcmyxc.util.ImageUtil;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 17:21
 * @description : DetailListAdapter
 */
public class DetailListAdapter extends RecyclerView.Adapter {

    private Context context;
    private Channel channel;
    private AlbumList albumList = new AlbumList();
    private int columns;

    public DetailListAdapter(Context context, Channel channel) {
        this.context = context;
        this.channel = channel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.detail_list_item, null);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        view.setTag(itemViewHolder);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (albumList.size() == 0) {
            return;
        }

        Album album = getItem(i);
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            itemViewHolder.albumName.setText(album.getTitle());

            // 没有值就不显示tip
            if (album.getTip().isEmpty()) {
                itemViewHolder.albumTip.setVisibility(View.GONE);
            } else {
                itemViewHolder.albumTip.setText(album.getTip());
            }

            Point point = ImageUtil.getVerPostSize(context, columns);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x, point.y);
            itemViewHolder.albumPoster.setLayoutParams(params);
            // 如果有竖图
            if (album.getVerImgUrl() != null) {
                ImageUtil.disPlayImage(itemViewHolder.albumPoster, album.getVerImgUrl(), point.x, point.y);
            } else if(album.getHorImgUrl() != null){// 横图
                ImageUtil.disPlayImage(itemViewHolder.albumPoster, album.getHorImgUrl(), point.x, point.y);
            }
            else {
                // TODO 默认图
            }

            // 设置九宫格的点击事件
            int channelId = channel.getChannelId();
            itemViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (channelId){
                        case Channel.DOCUMENTARY:
                        case Channel.COMIC:
                        case Channel.MOVIE:
                        case Channel.VARIETY:
                        case Channel.MUSIC:
                            AlbumDetailActivity.launch((Activity) context, album, 0, true);
                            break;
                        default:
                            AlbumDetailActivity.launch((Activity) context, album);
                            break;
                    }
                }
            });
        }

    }

    private Album getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public int getItemCount() {
        return Math.max(albumList.size(), 0);
    }

    // 设置显示的列数
    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setData(Album album) {
        albumList.add(album);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout container;
        private ImageView albumPoster;
        private TextView albumTip;
        private TextView albumName;

        public ItemViewHolder(@NonNull View view) {
            super(view);
            // 绑定视图下的组件
            container = view.findViewById(R.id.album_container);
            albumPoster = view.findViewById(R.id.iv_album_poster);
            albumTip = view.findViewById(R.id.tv_album_tip);
            albumName = view.findViewById(R.id.tv_album_name);
        }
    }

}