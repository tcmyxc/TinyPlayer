package com.tcmyxc.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcmyxc.R;
import com.tcmyxc.adapter.FavoriteAdapter;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;

import java.util.ArrayList;
import java.util.List;

import db.FavoriteDBHelper;

/**
 * @author : 徐文祥
 * @date : 2021/10/9 10:20
 * @description : 收藏Activity
 */
public class FavoriteActivity extends BaseActivity {

    private GridView favoriteGridView;
    private SwipeRefreshLayout swipeRefreshLayout;// 下拉刷新
    private AlbumList itemList = new AlbumList();
    private FavoriteDBHelper favoriteDBHelper;
    private TextView emptyView;
    private FavoriteAdapter favoriteAdapter;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, FavoriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(getResources().getString(R.string.favorite_title));

        emptyView = bindViewId(R.id.tv_empty);
        favoriteGridView = bindViewId(R.id.gv_favorite);
        swipeRefreshLayout = bindViewId(R.id.swipeRefreshLayout);
    }

    private GridView.OnScrollListener scrollListener = new GridView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean enable = false;
            // 组件课件并且有内容
            if(favoriteGridView != null && favoriteGridView.getChildCount() > 0){
                // 判断第一个item是否可见
                boolean isFirstItemVisible = favoriteGridView.getFirstVisiblePosition() == 0;
                // 判断第一个item离顶部是否为0
                boolean topOfFirstItemVisible = favoriteGridView.getChildAt(0).getTop() == 0;
                enable = isFirstItemVisible && topOfFirstItemVisible;// 两者都满足，才让刷新
                swipeRefreshLayout.setEnabled(enable);
            }
        }
    };

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refresh();
        }
    };

    private void refresh(){
        itemList = favoriteDBHelper.getAllData();
        isShowEmptyView();
        favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, itemList);
        favoriteGridView.setAdapter(favoriteAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void isShowEmptyView(){
        if(itemList.size() == 0){
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(getResources().getString(R.string.favorite_empty));
        }
        else{
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        favoriteDBHelper = new FavoriteDBHelper(this);
        itemList = favoriteDBHelper.getAllData();
        isShowEmptyView();

        favoriteGridView.setOnScrollListener(scrollListener);
        favoriteAdapter = new FavoriteAdapter(this, itemList);
        favoriteGridView.setAdapter(favoriteAdapter);

        // 设置刷新时控件颜色渐变
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        favoriteAdapter.setShowChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:// 处理左上角返回箭头
                finish();
                return true;
            case R.id.action_delete:// 删除
                if(favoriteAdapter.isSelected()){
                    showDeleteDialog();
                }
//                else {
//                    Toast.makeText(this, "未选中任何收藏项", Toast.LENGTH_LONG).show();
//                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // 删除收藏
                        deleteAllSelectedItem();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // 放弃删除
                        setShowChecked(false);
                        break;
                }
            }
        };
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getResources().getString(R.string.favorite_dialog_message));
        dialog.setPositiveButton(getResources().getString(R.string.favorite_dialog_sure), dialogListener);
        dialog.setNegativeButton(getResources().getString(R.string.favorite_dialog_quit), dialogListener);
        dialog.show();

    }

    private void deleteAllSelectedItem() {
        List<Integer> selectedList = new ArrayList<>();
        // 存储所有被选中项的索引
        for(int i=0; i<favoriteAdapter.getCount(); i++){
            FavoriteAdapter.FavoriteAlbum item = favoriteAdapter.getItem(i);
            if(item.isChecked()){
                selectedList.add(i);
            }
        }
        for(int position : selectedList){
            Album album = favoriteAdapter.getFavoriteAlbumList().get(position).getAlbum();
            // 统统删掉
            favoriteDBHelper.del(album.getAlbumId());
        }

        // 刷新
        refresh();
    }

    private void setShowChecked(boolean isSelected){
        if(!isSelected){
            favoriteAdapter.isCheckedAllItem(false);
        }
        favoriteAdapter.setShowChecked(isSelected);
        favoriteAdapter.notifyDataSetChanged();// 刷新数据
    }


}