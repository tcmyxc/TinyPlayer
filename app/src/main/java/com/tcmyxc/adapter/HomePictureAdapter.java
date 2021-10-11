package com.tcmyxc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.activity.AlbumDetailActivity;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.util.ImageUtil;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 11:28
 * @description : HomeAdapter
 */
public class HomePictureAdapter extends PagerAdapter {

    private Context context;
    private AlbumList albumList;

    public HomePictureAdapter(Activity activity, AlbumList albumList){
        context = activity;
        this.albumList = albumList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_picture_item, null);
        TextView textView = view.findViewById(R.id.tv_desc);
        ImageView imageView = view.findViewById(R.id.iv_img);

        if(albumList != null && albumList.size() > 0){
            Album album = albumList.get(position);
            textView.setText(album.getTitle());
            ImageUtil.disPlayImage(imageView, album.getHorImgUrl());

            container.addView(view);// 添加到容器中
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    // 固定5个，可以避免抛出异常
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
