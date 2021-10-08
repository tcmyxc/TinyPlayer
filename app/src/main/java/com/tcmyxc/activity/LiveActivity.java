package com.tcmyxc.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.tcmyxc.R;
import com.tcmyxc.adapter.LiveItemAdapter;
import com.tcmyxc.widget.CustomDecoration;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 14:08
 * @description : 直播Activity
 */
public class LiveActivity extends BaseActivity{

    private RecyclerView recyclerView;


    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, LiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(getResources().getString(R.string.live_title));

        recyclerView = bindViewId(R.id.ry_live);
        GridLayoutManager manager = new GridLayoutManager(this, 1);// 只需要一列
        //垂直布局排列
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(manager);
        // 设置分隔线
        recyclerView.addItemDecoration(new CustomDecoration(this));
        LiveItemAdapter adapter = new LiveItemAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);//回到第一个位置
    }

    @Override
    protected void initData() {

    }

    // 返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://actionbar 左边箭头id
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
