package com.tcmyxc.api;

import android.text.TextUtils;

import com.tcmyxc.AppManager;
import com.tcmyxc.listener.GetAlbumDetailListener;
import com.tcmyxc.listener.GetChannelAlbumListener;
import com.tcmyxc.listener.GetVideoListener;
import com.tcmyxc.listener.GetVideoPlayUrlListener;
import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.model.Channel;
import com.tcmyxc.model.ErrorInfo;
import com.tcmyxc.model.Site;
import com.tcmyxc.model.VideoList;
import com.tcmyxc.model.sohu.DetailResult;
import com.tcmyxc.model.sohu.Result;
import com.tcmyxc.model.sohu.ResultAlbum;
import com.tcmyxc.model.sohu.Video;
import com.tcmyxc.model.sohu.VideoData;
import com.tcmyxc.model.sohu.VideoResult;
import com.tcmyxc.util.LOG;
import com.tcmyxc.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 17:45
 * @description : 搜狐视频api
 */
public class SohuApi{

    private static final String TAG = SohuApi.class.getName();

    private static final int SOHU_CHANNELID_MOVIE = 1; // 搜狐电影频道ID
    private static final int SOHU_CHANNELID_SERIES = 2; // 搜狐电视剧频道ID
    private static final int SOHU_CHANNELID_VARIETY = 7; // 搜狐综艺频道ID
    private static final int SOHU_CHANNELID_DOCUMENTARY = 8; // 搜狐纪录片频道ID
    private static final int SOHU_CHANNELID_COMIC = 16; // 搜狐动漫频道ID
    private static final int SOHU_CHANNELID_MUSIC = 24; // 搜狐音乐频道ID

    // 某一专辑详情
    //http://api.tv.sohu.com/v4/album/info/9112373.json?plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47
    private final static String API_KEY = ".json?plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47";
    private final static String API_ALBUM_INFO = "http://api.tv.sohu.com/v4/album/info/";
    //http://api.tv.sohu.com/v4/search/channel.json?cid=2&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47&page=1&page_size=1
    private final static String API_CHANNEL_ALBUM_FORMAT = "http://api.tv.sohu.com/v4/search/channel.json" +
            "?cid=%s&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&" +
            "sver=6.2.0&sysver=4.4.2&partner=47&page=%s&page_size=%s";
    // 视频地址
    //http://api.tv.sohu.com/v4/album/videos/9112373.json?page=1&page_size=50&order=0&site=1&with_trailer=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47
    private final static String API_ALBUM_VIDOES_FORMAT = "http://api.tv.sohu.com/v4/album/videos/" +
            "%s.json?page=%s&page_size=%s" +
            "&order=0&site=1&with_trailer=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47";
    // 播放url
    //http://api.tv.sohu.com/v4/video/info/3669315.json?site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&aid=9112373
    private final static String API_VIDEO_PLAY_URL_FORMAT = "http://api.tv.sohu.com/v4/video/info/" +
            "%s.json?site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&" +
            "aid=%s";
    // 真实url格式 m3u8
    //http://hot.vrs.sohu.com/ipad3669271_4603585256668_6870592.m3u8?plat=6uid=f5dbc7b40dad477c8516885f6c681c01&pt=5&prod=app&pg=1

    public static void onGetChannelAlbums(Channel channel, int pageNo, int pageSize, GetChannelAlbumListener listener) {
        String url = getChannelAlbumUrl(channel, pageNo, pageSize);
        // LOG.i(url);
        getChannelAlbumByUrl(url, listener);
    }

    // 网络请求
    private static void getChannelAlbumByUrl(final String url, final GetChannelAlbumListener listener) {
        OkHttpUtil.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info = buildErrorInfo(url, "getChannelAlbumsByUrl", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetChannelAlbumFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(url, "getChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetChannelAlbumFailed(info);
                    return;
                }

                // 取到数据映射Result
                Result result = AppManager.getGson().fromJson(response.body().string(), Result.class);
                // 转换ResultAlbum变成Album
                AlbumList albumList = toConvertAlbumList(result);
                // Album存到AlbumLis中
                if (albumList != null) {
                    if (albumList.size() > 0 && listener != null) {
                        listener.onGetChannelAlbumSuccess(albumList);
                    }
                } else {
                    ErrorInfo info = buildErrorInfo(url, "getChannelAlbumsByUrl", null, ErrorInfo.ERROR_TYPE_DATA_CONVERT);
                    listener.onGetChannelAlbumFailed(info);
                }
            }
        });
    }

    private static AlbumList toConvertAlbumList(Result result) {
        AlbumList albumList = null;
        // 如果请求到了数据
        if (result.getData().getResultAlbumList().size() > 0) {
            albumList = new AlbumList();
            for (ResultAlbum resultAlbum : result.getData().getResultAlbumList()) {
                Album album = new Album(Site.SOHU);
                album.setAlbumDesc(resultAlbum.getTvDesc());
                album.setAlbumId(resultAlbum.getAlbumId());
                album.setHorImgUrl(resultAlbum.getHorHighPic());
                album.setMainActor(resultAlbum.getMainActor());
                album.setTip(resultAlbum.getTip());
                album.setTitle(resultAlbum.getAlbumName());
                album.setVerImgUrl(resultAlbum.getVerHighPic());
                album.setDirector(resultAlbum.getDirector());

                albumList.add(album);
            }
        }

        return albumList;
    }

    private static ErrorInfo buildErrorInfo(String url, String functionName, Exception e, int type) {
        ErrorInfo info = new ErrorInfo(Site.SOHU, type);
        info.setExceptionString(e.getMessage());
        info.setFunctionName(functionName);
        info.setUrl(url);
        info.setTag(TAG);
        info.setClassName(TAG);
        return info;
    }

    private static String getChannelAlbumUrl(Channel channel, int pageNo, int pageSize) {
        return String.format(API_CHANNEL_ALBUM_FORMAT, toConvertChannelId(channel), pageNo, pageSize);
    }

    private static int toConvertChannelId(Channel channel) {
        int channelId = -1;
        switch (channel.getChannelId()) {
            case Channel.SHOW:
                channelId = SOHU_CHANNELID_SERIES;
                break;
            case Channel.MOVIE:
                channelId = SOHU_CHANNELID_MOVIE;
                break;
            case Channel.COMIC:
                channelId = SOHU_CHANNELID_COMIC;
                break;
            case Channel.MUSIC:
                channelId = SOHU_CHANNELID_MUSIC;
                break;
            case Channel.DOCUMENTARY:
                channelId = SOHU_CHANNELID_DOCUMENTARY;
                break;
            case Channel.VARIETY:
                channelId = SOHU_CHANNELID_VARIETY;
                break;
            default:
                break;
        }
        return channelId;
    }

    public static void getAlbumDetail(Album album, final GetAlbumDetailListener listener){
        final String url = API_ALBUM_INFO + album.getAlbumId() + API_KEY;
        OkHttpUtil.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info = buildErrorInfo(url, "getAlbumDetail", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetAlbumDetailFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(url, "getAlbumDetail", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetAlbumDetailFailed(info);
                    return;
                }

                // 处理数据（详情页面）
                DetailResult result = AppManager.getGson().fromJson(response.body().string(), DetailResult.class);
                ResultAlbum albumDetail = result.getData();
                if(albumDetail != null){
                    if(albumDetail.getTotalVideoCount() > 0){
                        album.setVideoTotal(albumDetail.getTotalVideoCount());
                    }
                    else{
                        album.setVideoTotal(albumDetail.getLastVideoCount());
                    }
                }

                // 通知listener
                // 这里是通过回调函数进行数据的补全
                if(listener != null){
                    listener.onGetAlbumDetailSuccess(album);
                }

            }
        });
    }

    public static void getVideo(Album album, int pageSize, int pageNo, final GetVideoListener listener) {
        final String url = String.format(API_ALBUM_VIDOES_FORMAT, album.getAlbumId(), pageNo, pageSize);
        OkHttpUtil.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    ErrorInfo info = buildErrorInfo(url, "getVideo", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetVideoFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(url, "getVideo", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetVideoFailed(info);
                    return;
                }

                // 处理数据（视频信息）
                // LOG.d(TAG + ".getVideo " + response.body().string());
                VideoResult videoResult = AppManager.getGson().fromJson(response.body().string(), VideoResult.class);
                VideoData data = videoResult.getData();
                VideoList videoList = new VideoList();
                if(data != null){
                    for(Video video : data.getVideoList()) {
                        Video v = new Video();
                        v.setHorHighPic(video.getHorHighPic());
                        v.setVerHighPic(video.getVerHighPic());
                        v.setVid(video.getVid());
                        v.setAid(video.getAid());
                        v.setVideoName(video.getVideoName());
                        videoList.add(v);
                    }

                    if(listener != null){
                        listener.onGetVideoSuccess(videoList);
                    }
                }
            }
        });
    }

    public static void getVideoPlayUrl(Video video, GetVideoPlayUrlListener listener) {
        final String url = String.format(API_VIDEO_PLAY_URL_FORMAT, video.getVid(), video.getAid());
        OkHttpUtil.excute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (listener != null) {
                    ErrorInfo info = buildErrorInfo(url, "getVideoPlayUrl", e, ErrorInfo.ERROR_TYPE_URL);
                    listener.onGetFailed(info);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(url, "getVideoPlayUrl", null, ErrorInfo.ERROR_TYPE_HTTP);
                    listener.onGetFailed(info);
                    return;
                }

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    JSONObject data = result.optJSONObject("data");
                    // 获取url
                    String normalUrl = data.optString("url_nor");
                    String highUrl = data.optString("url_high");
                    String superUrl = data.optString("url_super");
                    if(!TextUtils.isEmpty(normalUrl)){
                        normalUrl += "uid=" + getUUID() + "&pt=5&prod=app&pg=1";
                        video.setNormalUrl(normalUrl);
                        LOG.d(": video normalUrl " + normalUrl);
                        // 发通知
                        if(listener != null){
                            listener.onGetNormalUrl(video, normalUrl);
                        }
                    }
                    if(!TextUtils.isEmpty(highUrl)){
                        highUrl += "uid=" + getUUID() + "&pt=5&prod=app&pg=1";
                        video.setHighUrl(highUrl);
                        // 发通知
                        if(listener != null){
                            listener.onGetHighUrl(video, highUrl);
                        }
                    }
                    if(!TextUtils.isEmpty(superUrl)){
                        superUrl += "uid=" + getUUID() + "&pt=5&prod=app&pg=1";
                        video.setSuperUrl(superUrl);
                        // 发通知
                        if(listener != null){
                            listener.onGetSuperUrl(video, superUrl);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
