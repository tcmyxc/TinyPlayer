package com.tcmyxc.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.tcmyxc.R;
import com.tcmyxc.adapter.SitePagerAdapter;
import com.tcmyxc.fragment.DetailListFragment;
import com.tcmyxc.model.Channel;

import java.util.HashMap;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 14:29
 * @description : 九宫格一般的Activity
 */
public class DetailListActivity extends BaseActivity{

    public static final String CHANNEL_ID = "channelId";

    private int channelId;
    private ViewPager viewPager;

    public static void launchDetailListActivity(Context context, int channelId) {
        Intent intent = new Intent(context, DetailListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(CHANNEL_ID, channelId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_list;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if(intent != null){
            channelId = intent.getIntExtra(CHANNEL_ID, 0);
        }
        Channel channel = new Channel(channelId, this);
        String channelName = channel.getChannelName();

        setSupportActionBar();// 支持toolbar
        setTitle(channelName);// 设置toolbar标题
        setSupportArrowActionBar(true);// 支持返回

        // 设置pager
        viewPager = bindViewId(R.id.detail_list_view_pager);
        viewPager.setAdapter(new SitePagerAdapter(getSupportFragmentManager(), this, channelId));
    }

    @Override
    protected void initData() {

    }

    // 处理左上角返回箭头
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
