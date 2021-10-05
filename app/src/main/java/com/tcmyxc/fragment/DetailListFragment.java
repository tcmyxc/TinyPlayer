package com.tcmyxc.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tcmyxc.R;
import com.tcmyxc.adapter.DetailListAdapter;
import com.tcmyxc.api.GetChannelAlbumListener;
import com.tcmyxc.api.SohuApi;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.model.Channel;
import com.tcmyxc.model.ErrorInfo;
import com.tcmyxc.util.LOG;
import com.tcmyxc.widget.PullLoadRecycleView;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 14:53
 * @description : todo
 */
public class DetailListFragment extends BaseFragment {

    private static int mSiteId;
    private static int mChannelId;
    private static final String CHANNEL_ID = "channelId";
    private static final String SITE_ID = "siteId";
    private PullLoadRecycleView pullLoadRecycleView;
    private TextView emptyView;
    private int columns;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final int REFRESH_DURATION = 1500;
    private static final int LOAD_MORE_DURATION = 3000;
    private int pageNo;
    private int pageSize = 30;
    private DetailListAdapter adapter;

    public DetailListFragment() {
    }

    public static Fragment newInstance(int siteId, int channelId) {
        DetailListFragment fragment = new DetailListFragment();

        Bundle bundle = new Bundle();// 传值用，fragment需要知道这些信息
        bundle.putInt(SITE_ID, siteId);
        bundle.putInt(CHANNEL_ID, channelId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 做一些初始化
        // 如果传的有数据
        if (getArguments() != null) {
            mSiteId = getArguments().getInt(SITE_ID);
            mChannelId = getArguments().getInt(CHANNEL_ID);
        }
        LOG.d("siteId is " + mSiteId + ", channelId is " + mChannelId);
        pageNo = 0;
        adapter = new DetailListAdapter(getActivity(), new Channel(mChannelId, getActivity()));
        loadData();// 初次加载数据
        // 分三栏
        columns = 3;
        adapter.setColumns(columns);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_list;
    }

    @Override
    protected void initView() {
        emptyView = bindViewId(R.id.tv_empty);
        emptyView.setText(getActivity().getResources().getString(R.string.load_more_text));
        pullLoadRecycleView = bindViewId(R.id.pull_load_recycler_view);
        pullLoadRecycleView.setGridLayout(columns);
        pullLoadRecycleView.setAdapter(adapter);
        pullLoadRecycleView.setOnPullLoadMoreListener(new PullLoadMoreListener());
    }

    @Override
    protected void initData() {

    }

    private void refreshData() {
        LOG.i("refreshData");
        // 请求结口，加载数据
        pageNo = 0;
        adapter = null;
        adapter = new DetailListAdapter(getActivity(), new Channel(mChannelId, getActivity()));
        loadData();// 加载数据
        // 分三栏
        columns = 3;
        adapter.setColumns(columns);
        // 下拉刷新
        pullLoadRecycleView.setAdapter(adapter);
        Toast.makeText(getActivity(), "已是最新数据", Toast.LENGTH_LONG).show();
    }

    private void loadData() {
        pageNo++;
        LOG.d(this.getClass().getName() + ".loadData: " + "pageNo is " + pageNo);
        // 请求结口，加载更多数据
        new SohuApi().onGetChannelAlbums(new Channel(mChannelId, getActivity()), pageNo, pageSize, new GetChannelAlbumListener() {
            @Override
            public void onGetChannelAlbumSuccess(AlbumList albumList) {
                LOG.i("albumList size: " + albumList.size());
                // 设置空白页面不可见
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyView.setVisibility(View.GONE);
                    }
                });

                // 处理数据
                for (Album album : albumList) {
                    // LOG.d(album.toString());
                    adapter.setData(album);
                }

                // 刷新页面
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onGetChannelAlbumFailed(ErrorInfo errorInfo) {
                // 显示错误信息
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyView.setText(getActivity().getResources().getString(R.string.data_failed_tip));
                    }
                });
            }
        });
    }

    class PullLoadMoreListener implements PullLoadRecycleView.OnPullLoadMoreListener {

        @Override
        public void refresh() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshData();
                    pullLoadRecycleView.setRefreshCompleted();
                }
            }, REFRESH_DURATION);
        }

        @Override
        public void loadMore() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                    pullLoadRecycleView.setLoadMoreCompleted();
                }
            }, LOAD_MORE_DURATION);
        }
    }

}
