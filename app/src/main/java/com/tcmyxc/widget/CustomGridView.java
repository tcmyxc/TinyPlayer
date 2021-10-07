package com.tcmyxc.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 徐文祥
 * @date : 2021/10/6 14:44
 * @description : 自定义GridView
 */
public class CustomGridView extends GridView {

    private Context context;
    private List<ViewHolder> footerViewList = new ArrayList<>();
    private boolean isLoading;
    private boolean hasMoreItem;
    private LoadMoreListener loadMoreListener;
    private ScrolledListener scrollListener;

    public CustomGridView(Context context) {
        super(context);
        initView (context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView (context);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView (context);
    }

    // 加载更多
    public interface LoadMoreListener {
        void onLoadMoreItems();
    }

    public interface ScrolledListener {
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    private void initView(Context context) {
        this.context = context;
        isLoading = false;
        // 添加LoadingView
        LoadingView loadingView = new LoadingView(context);
        addFooterView(loadingView);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollListener != null) {
                    scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                if (totalItemCount > 0) {
                    int lastViewVisible = firstVisibleItem + visibleItemCount;
                    // 1.不是正在加载中
                    // 2.已经加载到最后了
                    // 3.当前可见的最后一个item==总视图可见item
                    if (!isLoading && hasMoreItem && lastViewVisible == totalItemCount) {
                        if (loadMoreListener != null) {
                            loadMoreListener.onLoadMoreItems();
                        }
                    }
                }
            }
        });
    }

    // 添加footerView
    public void addFooterView(View view, Object data, boolean isSelcted) {
        ViewHolder holder = new ViewHolder();
        FrameLayout fl = new FullWidthViewLayout(context);
        fl.addView(view);
        holder.view = view;
        holder.data = data;
        holder.viewContainer = fl;
        holder.isSelected = isSelcted;
        footerViewList.add(holder);
    }

    public void addFooterView(View view) {
        addFooterView(view, null, true);
    }

    // 移除footerview
    public void removeFooterView(View v) {
        if (footerViewList.size() > 0) {
            removeHolder(v, footerViewList);
        }
    }

    private void removeHolder(View view, List<ViewHolder> list) {
        for (int i = 0; i < list.size(); i++) {
            ViewHolder holder = list.get(i);
            if (holder.view == view) {
                list.remove(i);
                break;
            }
        }
    }

    public void notifyChanged() {
        this.requestLayout();
        this.invalidate();
    }

    // FooterView容器
    class ViewHolder{
        public View view;
        public ViewGroup viewContainer;
        public Object data;
        public boolean  isSelected;
    }

    // 用于FooterView填充整个宽度
    class FullWidthViewLayout extends FrameLayout {

        public FullWidthViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int targetWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            MeasureSpec.makeMeasureSpec(targetWidth, MeasureSpec.getMode(widthMeasureSpec));
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isHasMoreItem() {
        return hasMoreItem;
    }

    public void setHasMoreItem(boolean hasMoreItem) {
        this.hasMoreItem = hasMoreItem;
    }

    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public ScrolledListener getScrollListener() {
        return scrollListener;
    }

    public void setScrollListener(ScrolledListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}

