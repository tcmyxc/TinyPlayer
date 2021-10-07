package com.tcmyxc.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.tcmyxc.R;
import com.tcmyxc.adapter.VideoItemAdapter;
import com.tcmyxc.adapter.VideoSelectedListener;
import com.tcmyxc.api.GetVideoListener;
import com.tcmyxc.api.SohuApi;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.ErrorInfo;
import com.tcmyxc.model.VideoList;
import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.util.LOG;
import com.tcmyxc.widget.CustomGridView;

/**
 * @author : 徐文祥
 * @date : 2021/10/5 21:22
 * @description : 详情页展示集数的Fragment
 */
public class AlbumPlayGridFragment extends BaseFragment{

    private static final String TAG = AlbumPlayGridFragment.class.getName();

    private static final String ARGS_ALBUM = "album";
    private static final String ARGS_IS_SHOE_DESC = "isShowDesc";
    private static final String ARGS_INTI_POSITION = "initVideoPositon";

    private Album album;
    private boolean isShowDesc;
    private int initVideoPositon;
    private int pageNo;
    private int pageSize;
    private VideoItemAdapter videoItemAdapter;
    private CustomGridView customGridView;
    private int pageTotal;
    private View emptyView;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isFirstSelection = true;
    private int currentPosition;
    private PlayVideoSelectedListener playVideoSelectedListener;

    public AlbumPlayGridFragment() {
    }

    public static AlbumPlayGridFragment newInstance(Album album, boolean isShowDesc, int initVideoPositon){
        AlbumPlayGridFragment fragment = new AlbumPlayGridFragment();
        // 存数据
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ALBUM, album);
        bundle.putBoolean(ARGS_IS_SHOE_DESC, isShowDesc);
        bundle.putInt(ARGS_INTI_POSITION, initVideoPositon);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface PlayVideoSelectedListener{
        void OnPlayVideoSelected(Video video, int position);
    }

    private VideoSelectedListener videoSelectedListener = new VideoSelectedListener() {
        @Override
        public void onVideoSelected(Video video, final int position) {
            if(customGridView != null) {
                // 选中点的一集
                LOG.d(TAG + ".videoSelectedListener: 选中的集数是: " + position);
                customGridView.setSelection(position);
                customGridView.setItemChecked(position, true);
                currentPosition = position;
                // 如果被设置过，就回调到上一层
                if(playVideoSelectedListener != null){
                    playVideoSelectedListener.OnPlayVideoSelected(video, position);
                }
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album_desc;
    }

    @Override
    protected void initView() {
        emptyView = bindViewId(R.id.tv_empty);
        emptyView.setVisibility(View.VISIBLE);

        customGridView = bindViewId(R.id.gv_video_layout);
        customGridView.setNumColumns(isShowDesc ? 1 : 6);// 1是纪录片
        customGridView.setAdapter(videoItemAdapter);
        // 总集数大于能展示的pageSize，肯定要加载更多
        if(album.getVideoTotal() > 0 && album.getVideoTotal() > pageSize){
            customGridView.setHasMoreItem(true);
        }
        else {
            customGridView.setHasMoreItem(false);
        }
        // 加载更多
        customGridView.setLoadMoreListener(new CustomGridView.LoadMoreListener() {
            @Override
            public void onLoadMoreItems() {
                loadData();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            album = getArguments().getParcelable(ARGS_ALBUM);
            isShowDesc = getArguments().getBoolean(ARGS_IS_SHOE_DESC);
            initVideoPositon = getArguments().getInt(ARGS_INTI_POSITION);
            pageSize = 50;
            pageNo = 0;
            currentPosition = initVideoPositon;
            videoItemAdapter = new VideoItemAdapter(getActivity(), album.getVideoTotal(), videoSelectedListener);
            videoItemAdapter.setIsShowTitleContent(isShowDesc);
            pageTotal = (album.getVideoTotal() + pageSize - 1) / pageSize;

            loadData();
        }
    }

    private void loadData() {
        pageNo++;
        SohuApi.getVideo(album, pageSize, pageNo, new GetVideoListener() {

            @Override
            public void onGetVideoSuccess(VideoList videoList) {
                LOG.d(TAG + ".onGetVideoSuccess " + videoList.size());
                // 添加数据
                for(Video video : videoList){
                    videoItemAdapter.addVideo(video);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        emptyView.setVisibility(View.GONE);
                        // 显示内容
                        if(videoItemAdapter != null){
                            videoItemAdapter.notifyDataSetChanged();
                        }
                        if(videoItemAdapter.getCount() > initVideoPositon && isFirstSelection){
                            customGridView.setSelection(initVideoPositon);
                            customGridView.setItemChecked(initVideoPositon, true);
                            isFirstSelection = false;
                            SystemClock.sleep(100);
                            customGridView.smoothScrollToPosition(initVideoPositon);
                        }
                    }
                });
            }

            @Override
            public void onGetVideoFailed(ErrorInfo errorInfo) {

            }
        });
    }

    public void setPlayVideoSelectedListener(PlayVideoSelectedListener playVideoSelectedListener) {
        this.playVideoSelectedListener = playVideoSelectedListener;
    }
}
