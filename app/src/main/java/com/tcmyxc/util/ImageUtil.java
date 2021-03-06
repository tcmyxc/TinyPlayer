package com.tcmyxc.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 22:44
 * @description : 图片工具类
 */
public class ImageUtil {

    private static final float VER_POSTER_RATIO = 0.73f;
    private static final float HOR_POSTER_RATIO = 1.5f;

    public static void disPlayImage(ImageView view, String url) {
        if (view != null && url != null) {
            Glide.with(view.getContext())
                    .load(url)
                    .into(view);
        }
    }

    public static void disPlayImage(ImageView view, String url, int width, int height) {
        if (view != null && url != null && width > 0 && height > 0) {
            if (width > height) {
                Glide.with(view.getContext())
                        .load(url) //加载图片url
                        .diskCacheStrategy(DiskCacheStrategy.ALL)// 设置缓存
                        .error(R.drawable.ic_loading_hor)// 出错时使用默认图
                        .fitCenter()// 设置图片居中,fitCenter居中自适应
                        .override(height, width)// 重写宽高
                        .into(view);// 加载到imageview上
            } else {
                Glide.with(view.getContext())
                        .load(url) // 加载图片url
                        .diskCacheStrategy(DiskCacheStrategy.ALL)// 设置缓存
                        .error(R.drawable.ic_loading_hor)// 出错时使用默认图
                        .centerCrop()// 设置图片居中,centerCrop会截断大图,不会自适应,
                        .override(width, height)// 重写宽高
                        .into(view);// 加载到imageview上
            }
        }
    }

    // 让竖图图片获得最佳比例
    public static Point getVerPostSize(Context context, int columns) {
        int width = getScreenWidthPixel(context) / columns;
        width = width - (int) context.getResources().getDimension(R.dimen.dimen_8dp);
        int height = Math.round((float) width / VER_POSTER_RATIO);
        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }

    // 让横图图片获得最佳比例
    public static Point getHorPostSize(Context context, int columns) {
        int width = getScreenWidthPixel(context) / columns;
        width = width - (int) context.getResources().getDimension(R.dimen.dimen_8dp);
        int height = Math.round((float) width / HOR_POSTER_RATIO);
        Point point = new Point();
        point.x = width;
        point.y = height;
        return point;
    }

    public static int getScreenWidthPixel(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        return width;
    }
}
