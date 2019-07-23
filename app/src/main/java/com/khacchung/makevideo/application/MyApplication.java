package com.khacchung.makevideo.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.handler.CreatedListener;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.service.CreateVideoService;

import java.io.File;
import java.util.ArrayList;

public class MyApplication extends Application {
    public static final int VIDEO_WIDTH = 720;
    public static final int VIDEO_HEIGHT = 480;
    private static MyApplication instance;

    private boolean isAlertSound = false;

    private String pathSaveTempImage;

    private float timeLoad = 1f;
    private String frameVideo = "";
    private THEMES seletedTheme = THEMES.Shine;
    private MyMusicModel myMusicModel = null;
    private String pathMusic = "";
    private boolean isEnd = true;

    private ArrayList<MyImageModel> listIamge = new ArrayList<>();

    private CreatedListener listener;

    public static MyApplication getInstance() {
        return instance;
    }

    public void initData() {
        timeLoad = 1f;
        frameVideo = "";
        seletedTheme = THEMES.Shine;
        myMusicModel = null;
        isEnd = true;
        pathMusic = "";
        isAlertSound = false;
        listIamge.clear();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pathSaveTempImage = MyPath.getPathTemp(this);
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
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
        this.pathMusic = "";
    }

    public boolean isAlertSound() {
        return isAlertSound;
    }

    public void setAlertSound(boolean alertSound) {
        isAlertSound = alertSound;
    }

    public String getPathSaveTempImage() {
        return pathSaveTempImage;
    }

    public void setPathSaveTempImage(String pathSaveTempImage) {
        this.pathSaveTempImage = pathSaveTempImage;
    }

    public String getPathMusic() {
        return pathMusic;
    }

    public void setPathMusic(String pathMusic) {
        this.pathMusic = pathMusic;
    }

    public void removeAllImage() {
        File file = new File(MyPath.getPathTemp(this));
        if (file.exists()) {
            File f[] = file.listFiles();
            for (File tmp : f) {
                tmp.delete();
            }
        }
    }

    public boolean checkServiceCreateVideoIsRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CreateVideoService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void registerListener(CreatedListener listener) {
        this.listener = listener;
    }

    public CreatedListener getListener() {
        return listener;
    }
}
