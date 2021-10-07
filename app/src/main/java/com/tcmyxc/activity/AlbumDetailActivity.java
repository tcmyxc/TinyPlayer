package com.tcmyxc.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcmyxc.AppManager;
import com.tcmyxc.R;
import com.tcmyxc.api.GetAlbumDetailListener;
import com.tcmyxc.api.GetVideoPlayUrlListener;
import com.tcmyxc.api.SohuApi;
import com.tcmyxc.fragment.AlbumPlayGridFragment;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.ErrorInfo;
import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.util.ImageUtil;
import com.tcmyxc.util.LOG;

/**
 * @author : 徐文祥
 * @date : 2021/10/5 19:21
 * @description : 详情页
 */
public class AlbumDetailActivity extends BaseActivity {

    private String TAG = this.getClass().getName();

    private Album album;
    private int videoNo;
    private boolean isShow;

    private ImageView albumImage;
    private TextView albumName;
    private TextView albumDirector;
    private TextView albumMainActor;
    private TextView albumPoster;
    private TextView albumDesc;
    private boolean isFavorite;

    private Button superBitstreamButton;
    private Button normalBitstreamButton;
    private Button highBitstreamButton;
    private int currentVideoPosition;

    private AlbumPlayGridFragment fragment;// 中间显示集数或者详情的控件

    public static void launch(Activity activity, Album album, int videoNo, boolean isShow) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 携带参数
        intent.putExtra("album", album);
        intent.putExtra("videoNo", videoNo);
        intent.putExtra("isShowDesc", isShow);
        // 跳转
        activity.startActivity(intent);
    }

    public static void launch(Activity activity, Album album) {
        Intent intent = new Intent(activity, AlbumDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("album", album);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_detail;
    }

    @Override
    protected void initView() {
        LOG.d(TAG + ": initView");
        Intent intent = getIntent();
        album = intent.getParcelableExtra("album");
        videoNo = intent.getIntExtra("videoNo", 0);
        isShow = intent.getBooleanExtra("isShowDesc", false);
        // 设置支持ActionBar和返回箭头
        setSupportActionBar();
        setSupportArrowActionBar(true);
        setTitle(album.getTitle());// 显示标题

        // 设置页面上小组件的内容
        albumImage = bindViewId(R.id.iv_album_image);
        albumName = bindViewId(R.id.tv_album_name);
        albumDirector = bindViewId(R.id.tv_album_director);// 导演
        albumMainActor = bindViewId(R.id.tv_album_mainactor);// 主演
        albumPoster = bindViewId(R.id.iv_album_poster);// 海报
        albumDesc = bindViewId(R.id.tv_album_desc);// 详情

        // 设置底部按钮点击事件
        superBitstreamButton = bindViewId(R.id.bt_super);
        superBitstreamButton.setOnClickListener(superOnClickListener);

        highBitstreamButton = bindViewId(R.id.bt_high);
        highBitstreamButton.setOnClickListener(highOnClickListener);

        normalBitstreamButton = bindViewId(R.id.bt_normal);
        normalBitstreamButton.setOnClickListener(normalOnClickListener);
    }

    private View.OnClickListener superOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private View.OnClickListener highOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private View.OnClickListener normalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleButtonClick(v);
        }
    };

    private void handleButtonClick(View view) {
        LOG.i(TAG + ": handleButtonClick，点击了播放按钮");
        Button btn = (Button) view;
        String url = (String) btn.getTag(R.id.key_video_url);
        int type = (int) btn.getTag(R.id.key_video_stream);
        Video video = (Video) btn.getTag(R.id.key_video);
        int curPosition = (int) btn.getTag(R.id.key_current_video_number);
        // 跳播放页
        // 先判断网络是否可用
        if (AppManager.isNetWorkAvailable()) {
            if (AppManager.isNetWorkWifiAvailable()) {
                Intent intent = new Intent(AlbumDetailActivity.this, PlayActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("type", type);
                intent.putExtra("currentPosition", curPosition);
                intent.putExtra("video", video);
                // 去播放页面
                startActivity(intent);
            }
        }
    }

    private void updateInfo(){
        albumName.setText(album.getTitle());

        // 导演
        if (!TextUtils.isEmpty(album.getDirector())) {
            albumDirector.setText("导演: " + album.getDirector());
            albumDirector.setVisibility(View.VISIBLE);
        } else {
            albumDirector.setVisibility(View.GONE);
        }

        // 主演
        if (!TextUtils.isEmpty(album.getMainActor())) {
            albumMainActor.setText("主演: " + album.getMainActor());
            albumMainActor.setVisibility(View.VISIBLE);
        } else {
            albumMainActor.setVisibility(View.GONE);
        }

        // 详情
        if (!TextUtils.isEmpty(album.getAlbumDesc())) {
            albumDesc.setText(album.getAlbumDesc());
            albumDesc.setVisibility(View.VISIBLE);
        } else {
            albumDesc.setVisibility(View.GONE);
        }

        // 海报图（搜狐只有竖图）
        ImageUtil.disPlayImage(albumImage, album.getVerImgUrl());
    }

    @Override
    protected void initData() {
        LOG.d(TAG + ": initData");
        updateInfo();

        SohuApi.getAlbumDetail(album, new GetAlbumDetailListener() {
            @Override
            public void onGetAlbumDetailSuccess(final Album albm) {
                LOG.i( TAG + ".initData " + "总集数是 " + album.getVideoTotal());
                LOG.i( TAG + ".initData: currentVideoPosition " + currentVideoPosition);
                album = albm;

                // 在主线程中处理fragment控件
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateInfo();
                        LOG.i( TAG + ".initData: 在主线程中处理fragment控件，currentVideoPosition " + currentVideoPosition);
                        // 处理显示集数或者详情的控件
                        fragment = AlbumPlayGridFragment.newInstance(album, isShow, 0);
                        fragment.setPlayVideoSelectedListener(playVideoSelectedListener);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, fragment);// 替换控件
                        transaction.commit();
                        getFragmentManager().executePendingTransactions();
                    }
                });
            }

            @Override
            public void onGetAlbumDetailFailed(ErrorInfo errorInfo) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // actionbar左边箭头id
            case android.R.id.home:
                finish();
                return true;
            // 本来是收藏但是点了
            case R.id.action_favorite_item:
                if (isFavorite) {
                    isFavorite = false;
                    invalidateOptionsMenu();
                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_unfavorite_item:// 原来没有收藏
                if (!isFavorite) {
                    isFavorite = true;
                    invalidateOptionsMenu();
                    Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 把可选的menu和当前视图联系起来
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_detail_menu, menu);
        return true;
    }

    // 收藏
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 是否收藏
        MenuItem favotiteItem = menu.findItem(R.id.action_favorite_item);
        MenuItem unFavotiteItem = menu.findItem(R.id.action_unfavorite_item);
        favotiteItem.setVisible(isFavorite);
        unFavotiteItem.setVisible(!isFavorite);
        return super.onPrepareOptionsMenu(menu);
    }

    private AlbumPlayGridFragment.PlayVideoSelectedListener playVideoSelectedListener = new AlbumPlayGridFragment.PlayVideoSelectedListener() {
        @Override
        public void OnPlayVideoSelected(Video video, final int position) {
            LOG.d(TAG + ": OnPlayVideoSelected，选中的位置是 " + position);
            currentVideoPosition = position;
            SohuApi.getVideoPlayUrl(video, videoPlayUrlListener);
        }
    };

    public class StreamType {
        public static final int SUPER = 1;
        public static final int HIGH = 2;
        public static final int NORMAL = 3;
    }

    private GetVideoPlayUrlListener videoPlayUrlListener = new GetVideoPlayUrlListener() {
        @Override
        public void onGetSuperUrl(final Video video, final String url) {
            // 异步回调，需要刷新ui
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    superBitstreamButton.setVisibility(View.VISIBLE);// btn可见
                    // 设置各种信息
                    superBitstreamButton.setTag(R.id.key_video_url, url);
                    superBitstreamButton.setTag(R.id.key_video, video);
                    superBitstreamButton.setTag(R.id.key_current_video_number, currentVideoPosition);
                    superBitstreamButton.setTag(R.id.key_video_stream, StreamType.SUPER);
                }
            });
        }

        @Override
        public void onGetHighUrl(final Video video, final String url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    highBitstreamButton.setVisibility(View.VISIBLE);// btn可见
                    // 设置各种信息
                    highBitstreamButton.setTag(R.id.key_video_url, url);
                    highBitstreamButton.setTag(R.id.key_video, video);
                    highBitstreamButton.setTag(R.id.key_current_video_number, currentVideoPosition);
                    highBitstreamButton.setTag(R.id.key_video_stream, StreamType.HIGH);
                }
            });
        }

        @Override
        public void onGetNormalUrl(final Video video, final String url) {
            // LOG.d("onGetNormalUrl " + url + " " + video.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    normalBitstreamButton.setVisibility(View.VISIBLE);// btn可见
                    // 设置各种信息
                    normalBitstreamButton.setTag(R.id.key_video_url, url);
                    normalBitstreamButton.setTag(R.id.key_video, video);
                    normalBitstreamButton.setTag(R.id.key_current_video_number, currentVideoPosition);
                    normalBitstreamButton.setTag(R.id.key_video_stream, StreamType.NORMAL);
                }
            });
        }

        @Override
        public void onGetFailed(ErrorInfo info) {
            // ui 操作在主线程中操作
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 视频播放源请求失败，则底部按钮不展示
                    hideAllButton();
                }
            });

        }
    };

    // 底部按钮隐藏
    private void hideAllButton() {
        superBitstreamButton.setVisibility(View.GONE);
        highBitstreamButton.setVisibility(View.GONE);
        normalBitstreamButton.setVisibility(View.GONE);
    }
}
