package com.tcmyxc.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.activity.AlbumDetailActivity;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.util.ImageUtil;
import com.tcmyxc.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 徐文祥
 * @date : 2021/10/9 10:44
 * @description : FavoriteAdapter
 */
public class FavoriteAdapter extends BaseAdapter {

    private static final String TAG = FavoriteAdapter.class.getName();

    private static final int TYPE_COUNT = 2;

    private Context context;
    private AlbumList itemList;
    private boolean showChecked;// 是否显示选中的小图标
    private List<FavoriteAlbum> favoriteAlbumList;

    public FavoriteAdapter(Context context, AlbumList itemList) {
        this.context = context;
        this.itemList = itemList;
        showChecked = false;
        favoriteAlbumList = new ArrayList<>();
        for (Album album : itemList) {
            favoriteAlbumList.add(new FavoriteAlbum(album));
        }
    }

    @Override
    public int getCount() {
        return favoriteAlbumList.size();
    }

    @Override
    public FavoriteAlbum getItem(int position) {
        return favoriteAlbumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return showChecked ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FavoriteAlbum item = getItem(position);
        Album album = item.album;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.favorite_item, null);
            holder = new ViewHolder();
            holder.albumName = convertView.findViewById(R.id.tv_album_name);
            holder.albumPost = convertView.findViewById(R.id.iv_album_poster);
            holder.checkBtn = convertView.findViewById(R.id.cb_favorite);
            holder.container = convertView.findViewById(R.id.favorite_container);
            if (showChecked) {
                holder.checkBtn.setVisibility(View.VISIBLE);
            } else {
                holder.checkBtn.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (favoriteAlbumList.size() > 0) {
            // 展示
            holder.albumName.setText(album.getTitle());
            Point point = ImageUtil.getVerPostSize(context, 3);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x, point.y);
            holder.albumPost.setLayoutParams(params);
            // 如果有竖图
            if (album.getVerImgUrl() != null) {
                ImageUtil.disPlayImage(holder.albumPost, album.getVerImgUrl(), point.x, point.y);
            }else if(album.getHorImgUrl() != null){// 横图
                ImageUtil.disPlayImage(holder.albumPost, album.getHorImgUrl(), point.x, point.y);
            }

            holder.checkBtn.setChecked(item.isChecked());

            LOG.d(TAG + " ：收藏页面，showChecked is " + showChecked);
            // 如果选中的小图标还未出现
            if (showChecked == false) {
                // 点击跳转详情页面
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlbumDetailActivity.launch((Activity) context, album);
                    }
                });

                // 长按选中
                holder.container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        LOG.d(TAG + " ：收藏页面，长按了 " + position);
                        item.setChecked(true);
                        setShowChecked(true);
                        favoriteAlbumList.get(position).setChecked(true);
                        holder.container.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();// 刷新
                        return true;
                    }
                });
            }
            // fix：选中一个后，选中其他收藏项目失效的问题
            else{
                holder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LOG.d(TAG + " ：收藏页面，按图片选中 " + position);
                        refreshItemStatus(item, holder);
                    }
                });
            }

            // 考虑用户通过右上角的图标选中的情形
            holder.checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LOG.d(TAG + " ：收藏页面，点右上角选中 " + position);
                    refreshItemStatus(item, holder);
                }
            });
        }

        return convertView;
    }

    private void refreshItemStatus(FavoriteAlbum item, ViewHolder holder) {
        // 先获取以前选中的状态
        boolean checked = item.isChecked();
        // 点击之后需要把状态取反
        item.setChecked(!checked);
        holder.checkBtn.setChecked(!checked);
    }

    public void isCheckedAllItem(boolean isChecked) {
        for (FavoriteAlbum item : favoriteAlbumList) {
            item.setChecked(isChecked);
        }
    }

    public void setShowChecked(boolean isSelected) {
        this.showChecked = isSelected;
    }

    public boolean isSelected() {
        return showChecked;
    }

    public List<FavoriteAlbum> getFavoriteAlbumList() {
        return favoriteAlbumList;
    }

    class ViewHolder {
        private ImageView albumPost;
        private CheckBox checkBtn;
        private TextView albumName;
        private RelativeLayout container;
    }

    public class FavoriteAlbum {
        private Album album;
        private boolean checked;

        public FavoriteAlbum(Album album) {
            this.album = album;
            checked = false;
        }

        public Album getAlbum() {
            return album;
        }

        public void setAlbum(Album album) {
            this.album = album;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
