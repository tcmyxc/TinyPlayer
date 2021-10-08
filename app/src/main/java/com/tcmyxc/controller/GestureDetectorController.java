package com.tcmyxc.controller;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.tcmyxc.listener.GestureListener;

/**
 * @author : 徐文祥
 * @date : 2021/10/8 16:50
 * @description : 播放页面手势检测Controller
 */
public class GestureDetectorController implements GestureDetector.OnGestureListener {

    private static final String TAG = GestureDetectorController.class.getName();

    private GestureDetector gestureDetector;
    private GestureListener gestureListener;
    private int width;
    private ScrollType currentType;

    public GestureDetectorController(Context context, GestureListener listener) {
        width = context.getResources().getDisplayMetrics().widthPixels;// 获取屏幕宽度
        gestureListener = listener;
        gestureDetector = new GestureDetector(context, this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    // 手指按下
    @Override
    public boolean onDown(MotionEvent e) {
        currentType = ScrollType.NOTHING;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    // 手指滑动
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (gestureListener != null) {
            if (currentType != ScrollType.NOTHING) {
                switch (currentType) {
                    case VERTICAL_LEFT:// 左边上下滑
                        gestureListener.onScrollVerticalLeft(distanceY, e1.getY() - e2.getY());
                        break;
                    case VERTICAL_RIGHT:// 右边上下滑
                        gestureListener.onScrollVerticalRight(distanceY, e1.getY() - e2.getY());
                        break;
                    case HORIZONTAL:// 水平滑动
                        gestureListener.onScrollHorizontal(distanceX, e2.getX() - e1.getX());
                        break;
                }
                return false;
            }
        }

        // 水平方向上滑动的多
        if (Math.abs(distanceY) <= Math.abs(distanceX)) {
            currentType = ScrollType.HORIZONTAL;
            gestureListener.onScrollStart(currentType);
            return false;
        }

        int i = width / 3;// 把屏幕三等分
        // 左边屏幕滑动判断
        if (e1.getX() <= i) {
            currentType = ScrollType.VERTICAL_LEFT;
            gestureListener.onScrollStart(currentType);
        } else if (e1.getX() > i * 2) {// 右滑判断
            currentType = ScrollType.VERTICAL_RIGHT;
            gestureListener.onScrollStart(currentType);
        } else {
            currentType = ScrollType.NOTHING;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public enum ScrollType {
        NOTHING,
        VERTICAL_LEFT,
        VERTICAL_RIGHT,
        HORIZONTAL
    }
}
