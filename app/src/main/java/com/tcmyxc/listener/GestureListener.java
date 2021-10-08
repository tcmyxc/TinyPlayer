package com.tcmyxc.listener;

import com.tcmyxc.controller.GestureDetectorController;

/**
 * @author : 徐文祥
 * @date : 2021/10/8 17:21
 * @description : GestureListener
 */
public interface GestureListener {

    void onScrollStart(GestureDetectorController.ScrollType type);// 开始滑动

    void onScrollHorizontal(float x1, float x2);// 水平滑动

    void onScrollVerticalLeft(float y1, float y2);// 左滑

    void onScrollVerticalRight(float y1, float y2);// 右滑

}
