package com.tcmyxc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author : 徐文祥
 * @date : 2021/10/8 20:44
 * @description : CustomDecoration
 */
public class CustomDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private static final int[] ATTR = new int[]{
            android.R.attr.listDivider
    };
    private Drawable divider;// 分隔条

    public CustomDecoration(Context context) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(ATTR);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizonalLine(c, parent, state);
    }

    private void drawHorizonalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            // 获取每一个子item的布局信息
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int top = childView.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    //设置每行都有divider
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, divider.getMinimumHeight());
    }
}
