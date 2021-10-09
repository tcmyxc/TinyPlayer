package com.tcmyxc.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.tcmyxc.AppManager;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 18:03
 * @description : todo
 */
public class Album implements Parcelable {

    private String albumId;// 专辑id
    private int videoTotal;// 集数
    private String title;// 专辑名称
    private String subTitle;// 专辑子标题
    private String director;// 导演
    private String mainActor;// 主演
    private String verImgUrl;// 专辑竖图
    private String horImgUrl;// 专辑横图
    private String albumDesc;// 专辑描述
    private Site site;// 网站
    private String tip;// 提示
    private boolean isCompleted;// 专辑是否更新完
    private Context context;

    public Album(int siteId) {
        site = new Site(siteId);
    }

    public Album(Parcel in) {
        this.albumId = in.readString();
        this.videoTotal = in.readInt();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.director = in.readString();
        this.mainActor = in.readString();
        this.verImgUrl = in.readString();
        this.horImgUrl = in.readString();
        this.albumDesc = in.readString();
        this.tip = in.readString();
        this.site = new Site(in.readInt());
        this.isCompleted = in.readByte() != 0;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumId);
        dest.writeInt(videoTotal);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(director);
        dest.writeString(mainActor);
        dest.writeString(verImgUrl);
        dest.writeString(horImgUrl);
        dest.writeString(albumDesc);
        dest.writeString(tip);
        dest.writeInt(site.getSiteId());
        dest.writeByte((byte) (isCompleted ? 1 : 0));
    }


    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getVideoTotal() {
        return videoTotal;
    }

    public void setVideoTotal(int videoTotal) {
        this.videoTotal = videoTotal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getMainActor() {
        return mainActor;
    }

    public void setMainActor(String mainActor) {
        this.mainActor = mainActor;
    }

    public String getVerImgUrl() {
        return verImgUrl;
    }

    public void setVerImgUrl(String verImgUrl) {
        this.verImgUrl = verImgUrl;
    }

    public String getHorImgUrl() {
        return horImgUrl;
    }

    public void setHorImgUrl(String horImgUrl) {
        this.horImgUrl = horImgUrl;
    }

    public String getAlbumDesc() {
        return albumDesc;
    }

    public void setAlbumDesc(String albumDesc) {
        this.albumDesc = albumDesc;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId='" + albumId + '\'' +
                ", videoTotal=" + videoTotal +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", director='" + director + '\'' +
                ", mainActor='" + mainActor + '\'' +
                ", verImgUrl='" + verImgUrl + '\'' +
                ", horImgUrl='" + horImgUrl + '\'' +
                ", albumDesc='" + albumDesc + '\'' +
                ", site=" + site +
                ", tip='" + tip + '\'' +
                ", isCompleted=" + isCompleted +
                ", context=" + context +
                '}';
    }

    public String toJson(){
        return AppManager.getGson().toJson(this);
    }

    public static Album fromJson(String json){
        return AppManager.getGson().fromJson(json, Album.class);
    }
}
