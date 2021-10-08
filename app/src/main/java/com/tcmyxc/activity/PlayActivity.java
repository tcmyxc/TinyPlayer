package com.tcmyxc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.util.DateUtil;
import com.tcmyxc.util.LOG;
import com.tcmyxc.widget.media.IjkVideoView;

import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * @author : 徐文祥
 * @date : 2021/10/6 19:36
 * @description : 视频播放页面
 */
public class PlayActivity extends BaseActivity {

    private static final String TAG = PlayActivity.class.getName();

    private static final int CHECK_TIME = 1;
    private static final int CHECK_BATTERY = 2;
    private static final int CHECK_PROGRESS = 3;
    private static final int AUTO_HIDE_TIME = 10000;

    private String url;
    private int stremType;
    private int currentPosition;
    private Video video;
    private IjkVideoView videoView;
    private RelativeLayout loadingLayout;
    private TextView loadingText;
    private FrameLayout topLayout;
    private LinearLayout bottomLayout;
    private ImageView backBtn;
    private TextView videoNameView;
    private TextView sysTimeView;
    private ImageView bigPauseBtn;
    private CheckBox playOrPauseBtn;
    private TextView currentVideoTime;
    private TextView totalVideoTime;
    private ImageView batteryView;
    private SeekBar playerSeekbar;
    private ImageView nextVideo;
    private TextView bitstreamView;
    private EventHandler eventHandler;
    private boolean isPanelShowing = false;
    private int batteryLevel;
    private boolean isMove = false;// 是否滑动了屏幕
    private Formatter formatter;
    private StringBuilder formatterStringBuilder;
    private boolean isDragging;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        LOG.i(TAG + ": 进入了播放页");
        setContentView(R.layout.activity_play);
        // 去信息
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        stremType = intent.getIntExtra("type", 0);
        currentPosition = intent.getIntExtra("currentPosition", 0);
        video = intent.getParcelableExtra("video");

        eventHandler = new EventHandler(Looper.myLooper());
        initTopAndBottomView();
        initListener();

        // 初始化播放器
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
                switch (what) {
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

        toggleTopAndBottomLayout();
    }

    private void initListener() {
        // 返回的点击事件
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 点屏幕中间暂停的点击事件
        bigPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                updatePlayPauseStatus(true);
            }
        });
        // 底部
        playOrPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayOrPause();
            }
        });
    }

    private void handlePlayOrPause() {
        // 如果视频正在播放，就暂停，反之亦然
        if(videoView.isPlaying()){
            videoView.pause();
            updatePlayPauseStatus(false);
        }
        else{
            videoView.start();
            updatePlayPauseStatus(true);
        }
    }

    private void updatePlayPauseStatus(boolean isPlaying) {
        bigPauseBtn.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
        playOrPauseBtn.invalidate();
        playOrPauseBtn.setChecked(isPlaying);
        playOrPauseBtn.refreshDrawableState();
    }

    private void initTopAndBottomView() {
        topLayout = bindViewId(R.id.fl_player_top_container);// 顶部容器
        backBtn = bindViewId(R.id.iv_player_close);// 返回按钮
        videoNameView = bindViewId(R.id.tv_player_video_name);// 剧名
        sysTimeView = bindViewId(R.id.tv_sys_time);// 系统时间
        batteryView = bindViewId(R.id.iv_battery);// 手机电量

        bigPauseBtn = bindViewId(R.id.iv_player_center_pause);// 中间的暂停

        bottomLayout = bindViewId(R.id.ll_player_bottom_layout);// 底部容器
        playOrPauseBtn = bindViewId(R.id.cb_play_pause);// 底部的暂停
        nextVideo = bindViewId(R.id.iv_next_video);
        currentVideoTime = bindViewId(R.id.tv_current_video_time);// 播放进度
        playerSeekbar = bindViewId(R.id.sb_player_seekbar);
        totalVideoTime = bindViewId(R.id.tv_total_video_time);
        bitstreamView = bindViewId(R.id.tv_bitstream);// 码流

        playerSeekbar.setMax(1000);
        playerSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        formatterStringBuilder = new StringBuilder();
        formatter = new Formatter(formatterStringBuilder, Locale.getDefault());
    }

    // playerSeekbar的listener
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // seekbar进度发生变化时回调
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(!fromUser){
                return;
            }
            long duration = videoView.getDuration();// 获取视频的时长
            long curPosition = duration * progress / 1000L;
            currentVideoTime.setText(strToTime((int) curPosition));
        }

        // seekbar开始拖动时回调
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDragging = true;
        }

        // seekbar拖动停止时回调
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isDragging = false;
            int progress = playerSeekbar.getProgress();// 拖动停止的进度
            long duration = videoView.getDuration();// 获取视频的时长
            long curPosition = duration * progress / 1000L;// 视频当前的进度
            videoView.seekTo((int) curPosition);
            eventHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTopAndBottomLayout();
                }
            }, AUTO_HIDE_TIME);
        }
    };

    private void updateProgress(){
        int curPosition = videoView.getCurrentPosition();// 视频当前的时间
        int duration = videoView.getDuration();// 获取视频的时长
        if(playerSeekbar != null){
            if(duration > 0){
                long pos = curPosition * 1000l / duration;
                playerSeekbar.setProgress((int) pos);
            }
            int percent = videoView.getBufferPercentage();// 缓冲的进度
            playerSeekbar.setSecondaryProgress(percent);// 设置缓冲进度
            currentVideoTime.setText(strToTime(curPosition));
            totalVideoTime.setText(strToTime(duration));
        }
    }

    private String strToTime(int timeMs){
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = (totalSeconds / 3600);
        formatterStringBuilder.setLength(0);
        if(hours > 0){
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }
        else{
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void toggleTopAndBottomLayout() {
        if (isPanelShowing) {
            hideTopAndBottomLayout();
        } else {
            showTopAndBottomLayout();
            // 界面友好，先显示顶部底部的工具栏，无操作后几秒后不显示
            eventHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTopAndBottomLayout();
                }
            }, AUTO_HIDE_TIME);
        }
    }

    private void showTopAndBottomLayout() {
        topLayout.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
        isPanelShowing = true;
        // 更新进度、系统电量
        updateProgress();// 更新进度
        if(eventHandler != null){
            eventHandler.removeMessages(CHECK_TIME);
            Message timeMsg = eventHandler.obtainMessage(CHECK_TIME);
            eventHandler.sendMessage(timeMsg);

            eventHandler.removeMessages(CHECK_BATTERY);
            Message batteryMsg = eventHandler.obtainMessage(CHECK_BATTERY);
            eventHandler.sendMessage(batteryMsg);

            eventHandler.removeMessages(CHECK_PROGRESS);
            Message progressMsg = eventHandler.obtainMessage(CHECK_PROGRESS);
            eventHandler.sendMessage(progressMsg);
        }
        // 显示码流
        switch (stremType){
            case AlbumDetailActivity.StreamType.SUPER:
                bitstreamView.setText(getResources().getString(R.string.stream_super));
                break;
            case AlbumDetailActivity.StreamType.HIGH:
                bitstreamView.setText(getResources().getString(R.string.stream_high));
                break;
            case AlbumDetailActivity.StreamType.NORMAL:
                bitstreamView.setText(getResources().getString(R.string.stream_normal));
                break;
        }
        //
    }

    private void hideTopAndBottomLayout() {
        // 拖拽的时候不隐藏
        if(isDragging){
            return;
        }
        topLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        isPanelShowing = false;
    }

    @Override
    protected void initData() {
        if (video != null) {
            videoNameView.setText(video.getVideoName());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(batteryReceiver != null){
            unregisterReceiver(batteryReceiver);
            batteryReceiver = null;
        }
    }

    // 通过广播获取系统电量
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryLevel = intent.getIntExtra("level", 0);
            LOG.d(TAG + ": batteryReceiver onReceive batteryLevel=" + batteryLevel);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(isMove == false){
                toggleTopAndBottomLayout();
            }
        }

        return super.onTouchEvent(event);
    }

    class EventHandler extends Handler {

        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_TIME:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sysTimeView.setText(DateUtil.getCurrentTime());
                        }
                    });
                    break;
                case CHECK_BATTERY:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setCurrentBattery();
                        }
                    });
                    break;
                case CHECK_PROGRESS:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long duration = videoView.getDuration();
                            long curDuration = playerSeekbar.getProgress() * duration / 1000l;
                            currentVideoTime.setText(strToTime((int) curDuration));
                        }
                    });
                    break;
            }
        }
    }

    // 设置电量icon
    private void setCurrentBattery() {
        LOG.d(TAG + ": setCurrentBattery level " + batteryLevel);
        if (batteryLevel > 0 && batteryLevel <= 10) {
            batteryView.setBackgroundResource(R.drawable.ic_battery_10);
        } else if (batteryLevel > 10 && batteryLevel <= 20) {
            batteryView.setBackgroundResource(R.drawable.ic_battery_20);
        } else if (batteryLevel > 20 && batteryLevel <= 50) {
            batteryView.setBackgroundResource(R.drawable.ic_battery_50);
        } else if (batteryLevel > 50 && batteryLevel <= 80) {
            batteryView.setBackgroundResource(R.drawable.ic_battery_80);
        } else if (batteryLevel > 80 && batteryLevel <= 100) {
            batteryView.setBackgroundResource(R.drawable.ic_battery_100);
        }
    }

    // 按返回键之后需要把页面杀掉
    @Override
    protected void onStop() {
        super.onStop();
        if(videoView != null){
            videoView.stopPlayback();
        }
    }
}