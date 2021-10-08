package com.tcmyxc.fragment;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hejunlin.superindicatorlibray.CircleIndicator;
import com.hejunlin.superindicatorlibray.LoopViewPager;
import com.tcmyxc.R;
import com.tcmyxc.activity.DetailListActivity;
import com.tcmyxc.activity.FavoriteActivity;
import com.tcmyxc.activity.HistoryActivity;
import com.tcmyxc.activity.LiveActivity;
import com.tcmyxc.adapter.ChannelAdapter;
import com.tcmyxc.adapter.HomePictureAdapter;
import com.tcmyxc.util.LOG;

/**
 * @author : 徐文祥
 * @date : 2021/10/2 15:42
 * @description : HomeFragment
 */
public class HomeFragment extends BaseFragment{

    private GridView gridView;// 九宫格布局

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        LoopViewPager loopViewPager = bindViewId(R.id.loop_viewpager);
        CircleIndicator indicator = bindViewId(R.id.indicator);
        gridView = bindViewId(R.id.gv_channel);

        loopViewPager.setAdapter(new HomePictureAdapter(getActivity()));
        loopViewPager.setLooperPic(true);// 自动轮播
        indicator.setViewPager(loopViewPager);// indicator需要viewpager

        gridView.setAdapter(new ChannelAdapter(getActivity()));// 设置适配器
        // 设置九宫格的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // LOG.i(this.getClass().getName() + ".onItemClick: " + "position is " + position);
                switch (position){
                    case 6:
                        // 跳转直播
                        LiveActivity.launch(getActivity());
                        break;
                    case 7:
                        // 跳转收藏
//                        FavoriteActivity.launch(getActivity());
                        break;
                    case 8:
                        // 跳转历史记录
//                        HistoryActivity.launch(getActivity());
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

    }
}
