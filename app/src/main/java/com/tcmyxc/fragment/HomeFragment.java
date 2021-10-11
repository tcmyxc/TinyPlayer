package com.tcmyxc.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.tcmyxc.R;
import com.tcmyxc.activity.DetailListActivity;
import com.tcmyxc.activity.FavoriteActivity;
import com.tcmyxc.activity.LiveActivity;
import com.tcmyxc.adapter.ChannelAdapter;
import com.tcmyxc.adapter.HomePictureAdapter;
import com.tcmyxc.api.SohuApi;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.util.LOG;

/**
 * @author : 徐文祥
 * @date : 2021/10/2 15:42
 * @description : HomeFragment
 */
public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getName();

    private GridView gridView;// 九宫格布局
    private AlbumList albumList;// 轮播图数据
    private LoopViewPager loopViewPager;
    private CircleIndicator indicator;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        loopViewPager = bindViewId(R.id.loop_viewpager);
        indicator = bindViewId(R.id.indicator);

        gridView = bindViewId(R.id.gv_channel);
        gridView.setAdapter(new ChannelAdapter(getActivity()));// 设置适配器
        // 设置九宫格的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LOG.i(this.getClass().getName() + ": onItemClick, position is " + position);
                switch (position) {
                    case 6:
                        // 跳转直播
                        LiveActivity.launch(getActivity());
                        break;
                    case 7:
                        // 跳转收藏
                        FavoriteActivity.launch(getActivity());
                        break;
                    default:
                        // 跳转对应频道
                        DetailListActivity.launchDetailListActivity(getActivity(), position + 1);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        albumList = SohuApi.getSomeData();
        // 轮播
        LOG.d(TAG + " :获取首页轮播图数据: " + albumList.size());
        loopViewPager.setAdapter(new HomePictureAdapter(getActivity(), albumList));
        loopViewPager.setLooperPic(true);// 自动轮播
        indicator.setViewPager(loopViewPager);// indicator需要viewpager
    }
}
