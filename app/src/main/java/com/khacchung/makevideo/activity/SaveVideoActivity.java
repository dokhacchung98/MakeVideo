package com.khacchung.makevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.File;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;
import nl.bravobit.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class SaveVideoActivity extends BaseActivity implements MyClickHandler {
    private static final String TAG = SaveVideoActivity.class.getName();
    private ActivitySaveVideoBinding binding;
    private MyApplication myApplication;

    private String uriVideoFrames = "";
    private float timeLoad;
    private Handler handler = new Handler();

    private boolean isFinish = false;
    private boolean isRunEnd = false;
    private boolean isSuccess = false;
    private boolean isShow = false;
    private boolean isDestinationFolder = false;

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

        uriVideoFrames = myApplication.getFrameVideo();
        timeLoad = myApplication.getTimeLoad();

        if (FFmpeg.getInstance(this).isSupported()) {
            saveVideo();
            handler.post(runnable);
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
            isDestinationFolder = false;
        } else {
            pathSave = MyPath.getPathSaveVideo(this)
                    + "video"
                    + System.currentTimeMillis() + ".mp4";
            isDestinationFolder = true;
        }
        String cmd[] = CommandStringFFmpeg.getCommandCreadVideoFromImageAndMusic(this, myApplication.getMyMusicModel(), timeLoad, pathSave);
        Log.e(TAG, cmd.toString());

        try {
            FFmpeg.getInstance(this).execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    if (isDestinationFolder) {
                        isSuccess = true;
                        if (isRunEnd) {
                            ShowLog(true);
                            process = 100;
                            binding.seekbarPoint.setPoints(100);
                            myApplication.removeAllImage();
                            myApplication.initData();
                        }
                        isFinish = true;
                    } else {
                        joinVideoFrame();
                    }
                }

                @Override
                public void onFailure(String message) {
                    if (isRunEnd) {
                        ShowLog(false);
                        process = 100;
                        binding.seekbarPoint.setPoints(100);
                        myApplication.initData();
                    }
                    isFinish = true;
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void joinVideoFrame() {
        File file = new File(MyPath.getPathTempVideo(this));
        if (!file.exists()) {
            file.mkdirs();
        }
        String pathSave = MyPath.getPathSaveVideo(this)
                + "video"
                + System.currentTimeMillis() + ".mp4";
        isDestinationFolder = true;
        String cmd[] = CommandStringFFmpeg.addVideoFrame(this, myApplication.getFrameVideo(), pathSave);
        if (cmd == null) {
            isFinish = true;
            if (isRunEnd) {
                process = 100;
                ShowLog(false);
            }
            return;
        }
        try {
            FFmpeg.getInstance(this).execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    isSuccess = true;
                    if (isRunEnd) {
                        ShowLog(true);
                    }
                    isFinish = true;
                    process = 100;
                    binding.seekbarPoint.setPoints(100);
                    myApplication.removeAllImage();
                    myApplication.initData();
                }

                @Override
                public void onFailure(String message) {
                    isSuccess = false;
                    if (isRunEnd) {
                        ShowLog(false);
                    }
                    process = 100;
                    isFinish = true;
                    myApplication.initData();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private int process = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            process++;
            if (process >= 100) {
                process = 100;
            }
            if (process >= 97 && !isFinish) {
                process = 97;
                handler.removeCallbacks(this);
                isRunEnd = true;
            } else if (process >= 97) {
                process = 100;
                isRunEnd = true;
            }
            binding.seekbarPoint.setPoints(process);
            handler.postDelayed(this, 100);
            if (process == 100) {
                ShowLog(isSuccess);
                handler.removeCallbacks(this);
                myApplication.initData();
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void ShowLog(boolean isSuccess) {
        if (!isShow) {
            if (isSuccess) {
                ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), "Lưu video thành công", true);
            } else {
                ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), "Lưu video thất bại", false);
            }
            isShow = true;
        }
    }
}
