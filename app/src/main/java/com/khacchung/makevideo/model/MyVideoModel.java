package com.khacchung.makevideo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khacchung.makevideo.BR;

public class MyVideoModel extends BaseObservable {
    private String pathVideo;
    private String pathThumbnail;

    public MyVideoModel() {
    }

    public MyVideoModel(String pathVideo, String pathThumbnail) {
        this.pathVideo = pathVideo;
        this.pathThumbnail = pathThumbnail;
    }

    @Bindable
    public String getPathVideo() {
        return pathVideo;
    }

    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
        notifyPropertyChanged(BR.pathVideo);
    }

    @Bindable
    public String getPathThumbnail() {
        return pathThumbnail;
    }

    public void setPathThumbnail(String pathThumbnail) {
        this.pathThumbnail = pathThumbnail;
        notifyPropertyChanged(BR.pathThumbnail);
    }
}
