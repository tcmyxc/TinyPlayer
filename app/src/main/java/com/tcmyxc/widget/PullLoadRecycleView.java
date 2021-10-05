package com.tcmyxc.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 15:48
 * @description : 下拉刷新
 */
public class PullLoadRecycleView extends LinearLayout {

    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;// 下拉刷新的组件
    private RecyclerView recyclerView;
    private View footView;
    private AnimationDrawable animationDrawable;
    private OnPullLoadMoreListener onPullLoadMoreListener;

    private boolean isRefresh = false;// 是否刷新，默认不刷新
    private boolean isLoadMore = false;// 是否加载更多，默认不加载更多

    public PullLoadRecycleView(Context context) {
        super(context);
        initView(context);
    }

    public PullLoadRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullLoadRecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor"})
    private void initView(Context ctx) {
        context = ctx;
        View view = LayoutInflater.from(context).inflate(R.layout.pull_load_more_layout, null);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // 设置刷新时控件颜色渐变
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefreshListener());

        // 处理 recycleView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);// 设置固定大小
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 默认动画
        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isRefresh || isLoadMore;
            }
        });

        recyclerView.setVerticalScrollBarEnabled(false);// 隐藏滚动条
        recyclerView.addOnScrollListener(new RecyclerViewOnScrollListener());

        footView = view.findViewById(R.id.footer_view);
        ImageView imageView = footView.findViewById(R.id.iv_load_img);
        imageView.setBackgroundResource(R.drawable.loading);
        animationDrawable = (AnimationDrawable) imageView.getBackground();

        TextView textView = footView.findViewById(R.id.tv_load_text);
        footView.setVisibility(View.GONE);

        this.addView(view);

    }

    // 设置recyclerView的列数
    public void setGridLayout(int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(context, spanCount);
        manager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    class SwipeRefreshLayoutOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            if (!isRefresh) {
                isRefresh = true;
                refreshData();
            }
        }
    }

    // 滑动监听
    class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstItem = 0;
            int lastItem = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int totalCnt = layoutManager.getItemCount();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                // 第一个完全可见的item
                firstItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                // 最后一个完全可见的item
                lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                if (firstItem == 0 || firstItem == RecyclerView.NO_POSITION) {
                    lastItem = gridLayoutManager.findLastVisibleItemPosition();
                }
            }

            if (!isLoadMore && // isLoadMore是false
                    totalCnt - 1 == lastItem &&
                    swipeRefreshLayout.isEnabled() && // 可被刷新
                    !isRefresh && // 当前不是处于刷新状态
                    (dx > 0 || dy > 0)) {
                isLoadMore = true;
                swipeRefreshLayout.setEnabled(false);// 加载更多数据的时候禁止下拉刷新
                loadMoreData();
            } else {
                swipeRefreshLayout.setEnabled(true);
            }
        }
    }

    private void loadMoreData() {
        if (onPullLoadMoreListener != null) {
            footView.animate()
                    .translationY(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            footView.setVisibility(View.VISIBLE);
                            animationDrawable.start();
                        }
                    }).start();
            invalidate();
            onPullLoadMoreListener.loadMore();
        }
    }

    private void refreshData() {
        if (onPullLoadMoreListener != null) {
            onPullLoadMoreListener.refresh();
        }
    }

    // 设置刷新完毕
    public void setRefreshCompleted() {
        isRefresh = false;
        setRefreshing(false);
    }

    // 设置是否正在刷新
    private void setRefreshing(boolean isRefreshing) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    // 设置加载完毕
    public void setLoadMoreCompleted() {
        isRefresh = false;
        isLoadMore = false;
        setRefreshing(false);
        footView.animate()
                .translationY(footView.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start();
    }

    public interface OnPullLoadMoreListener {
        void refresh();

        void loadMore();
    }

    public void setOnPullLoadMoreListener(OnPullLoadMoreListener listener) {
        onPullLoadMoreListener = listener;
    }
}
