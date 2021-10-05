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

/**
 * @author : 徐文祥
 * @date : 2021/10/3 11:28
 * @description : HomeAdapter
 */
public class HomePictureAdapter extends PagerAdapter {

    private Context context;

    private int[] img = new int[]{
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
    };

    private int[] desc = new int[]{
            R.string.a_name,
            R.string.b_name,
            R.string.c_name,
            R.string.d_name,
            R.string.e_name
    };

    public HomePictureAdapter(Activity activity){
        context = activity;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_picture_item, null);
        TextView textView = view.findViewById(R.id.tv_desc);
        ImageView imageView = view.findViewById(R.id.iv_img);

        textView.setText(desc[position]);
        imageView.setImageResource(img[position]);

        container.addView(view);// 添加到容器中

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
