package com.tcmyxc.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.util.LOG;
import com.tcmyxc.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * @author : 徐文祥
 * @date : 2021/10/6 19:36
 * @description : 视频播放页面
 */
public class PlayActivity extends BaseActivity {

    private String TAG = PlayActivity.class.getName();

    private String url;
    private int stremType;
    private int currentPosition;
    private Video video;
    private IjkVideoView videoView;
    private RelativeLayout loadingLayout;
    private TextView loadingText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        LOG.i("进入了播放页");
        setContentView(R.layout.activity_play);
        // 去信息
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        stremType = intent.getIntExtra("type", 0);
        currentPosition = intent.getIntExtra("currentPosition", 0);
        video = intent.getParcelableExtra("video");
        videoView = bindViewId(R.id.video_view);
        loadingLayout = bindViewId(R.id.rl_loading_layout);
        loadingText = bindViewId(R.id.tv_loading_info);
        loadingText.setText("正在加载...");
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                videoView.start();
            }
        });
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int i1) {
                switch (what){
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        loadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        loadingLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }
}