package com.khacchung.makevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivitySaveVideoBinding;
import com.khacchung.makevideo.extention.CommandStringFFmpeg;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.model.MyMusicModel;

import java.io.File;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;
import nl.bravobit.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class SaveVideoActivity extends BaseActivity implements MyClickHandler {
    private static final String TAG = SaveVideoActivity.class.getName();
    private ActivitySaveVideoBinding binding;
    private MyApplication myApplication;
    private String uriMusic = "";
    private String uriVideoFrames = "";
    private float timeLoad;
    private int sizeOfListImage = 0;
    private float totalTime;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, SaveVideoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_save_video);
        binding.setHandler(this);
        myApplication = MyApplication.getInstance();

        MyMusicModel myMusicModel = myApplication.getMyMusicModel();
        if (myMusicModel != null) {
            uriMusic = myMusicModel.getPathMusic();
        }
        uriVideoFrames = myApplication.getFrameVideo();
        timeLoad = myApplication.getTimeLoad();
        sizeOfListImage = myApplication.getListIamge().size();
        totalTime = timeLoad * (sizeOfListImage + 1);

        if (FFmpeg.getInstance(this).isSupported()) {
            saveVideo();
        } else {
            ShowLog.ShowLog(this, binding.getRoot(), "Điện thoại không hỗ trợ", false);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_cancel) {
            onBackPressed();
        }
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    private void saveVideo() {
        String pathSave;
        File file = new File(MyPath.getPathTempVideo(this));
        if (!file.exists()) {
            file.mkdirs();
        }
        if (uriVideoFrames.isEmpty()) {
            pathSave = MyPath.getPathTempVideo(this) + MyPath.NAME_VIDEO_TEMP;
        } else {
            pathSave = MyPath.getPathSaveVideo(this)
                    + "video"
                    + System.currentTimeMillis() + ".mp4";
        }
        String cmd[] = CommandStringFFmpeg.getCommandCreadVideoFromImageAndMusic(this, uriMusic, timeLoad, totalTime, pathSave);
        Log.e(TAG, cmd.toString());

        try {
            FFmpeg.getInstance(this).execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), "Lưu video thành công", true);
                    binding.seekbarPoint.setPoints(100);
                    myApplication.removeAllImage();
                }

                @Override
                public void onProgress(String message) {
                }

                @Override
                public void onFailure(String message) {
                    ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), "Lưu video thất bại", false);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
