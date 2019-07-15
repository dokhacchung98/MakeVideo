package com.khacchung.makevideo.application;

import android.app.Application;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;

import java.util.ArrayList;

public class MyApplication extends Application {
    public static final int VIDEO_WIDTH = 720;
    public static final int VIDEO_HEIGHT = 480;
    private static MyApplication instance;

    private String pathSaveTempImage;

    private float timeLoad = 2f;
    private String frameVideo = "";
    private THEMES seletedTheme = THEMES.CIRCLE_IN;
    private MyMusicModel myMusicModel = new MyMusicModel("android.resource://com.khacchung.makevideo/" + R.raw.a, "temp");

    private ArrayList<MyImageModel> listIamge = new ArrayList<>();

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pathSaveTempImage = MyPath.getPathTemp(this);
    }

    public float getTimeLoad() {
        return timeLoad;
    }

    public void setTimeLoad(float timeLoad) {
        this.timeLoad = timeLoad;
    }

    public String getFrameVideo() {
        return frameVideo;
    }

    public void setFrameVideo(String frameVideo) {
        this.frameVideo = frameVideo;
    }

    public ArrayList<MyImageModel> getListIamge() {
        return listIamge;
    }

    public void setListIamge(ArrayList<MyImageModel> listIamge) {
        this.listIamge = listIamge;
    }

    public void clearListImage() {
        this.listIamge.clear();
    }

    public void addItemInListImage(MyImageModel model) {
        listIamge.add(model);
    }

    public THEMES getSeletedTheme() {
        return seletedTheme;
    }

    public void setSeletedTheme(THEMES seletedTheme) {
        this.seletedTheme = seletedTheme;
    }

    public MyMusicModel getMyMusicModel() {
        return myMusicModel;
    }

    public void setMyMusicModel(MyMusicModel myMusicModel) {
        this.myMusicModel = myMusicModel;
    }

    public String getPathSaveTempImage() {
        return pathSaveTempImage;
    }

    public void setPathSaveTempImage(String pathSaveTempImage) {
        this.pathSaveTempImage = pathSaveTempImage;
    }
}
