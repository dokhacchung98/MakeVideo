package com.khacchung.makevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
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

    private static final int STEP_0 = 0;//create thumbnail
    private static final int STEP_1 = 1;//create video
    private static final int STEP_2 = 2;//add frame
    private static final int STEP_3 = 3;//finish

    private boolean isCancel = false;

    private int step = -1;
    private boolean isRunEnd = false;
    private boolean isShow = false;
    private boolean isSuccess = false;

    private boolean isShowLogRemove = false;

    private String nameVideo;
    private AlertDialog.Builder builder;

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

        nameVideo = "video"
                + System.currentTimeMillis();
        initAlert();
        File file = new File(MyPath.getPathSaveVideo(this));
        if (!file.exists()) {
            file.mkdirs();
        }

        if (FFmpeg.getInstance(this).isSupported()) {
            createThumbnail();
            handler.post(runnable);
        } else {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.mobile_not_sup), false);
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

    private void createThumbnail() {
        step = STEP_0;
        try {
            File file[] = new File(MyPath.getPathTemp(SaveVideoActivity.this)).listFiles();
            if (file.length > 0) {
                String cmd[] = CommandStringFFmpeg.copyThumbnail(SaveVideoActivity.this,
                        file[0].getAbsolutePath(), nameVideo);
                Log.e(TAG, "createThumbnail(): " + cmd.toString());

                FFmpeg.getInstance(SaveVideoActivity.this).execute(cmd, new ExecuteBinaryResponseHandler() {
                    @Override
                    public void onSuccess(String message) {
                        createVideo();
                    }

                    @Override
                    public void onFailure(String message) {
                        super.onFailure(message);
                        showLog();
                    }
                });
            }
        } catch (Exception e) {
            showLog();
        }
    }

    private void createVideo() {
        step = STEP_1;
        String pathSave;
        File file = new File(MyPath.getPathTempVideo(this));
        if (!file.exists()) {
            file.mkdirs();
        }

        String pathSound = "";
        if (myApplication.getPathMusic().isEmpty()) {
            if (myApplication.getMyMusicModel() != null) {
                pathSound = myApplication.getMyMusicModel().getPathMusic();
            }
        } else {
            pathSound = myApplication.getPathMusic();
        }

        pathSave = MyPath.getPathTempVideo(this) + MyPath.NAME_VIDEO_TEMP;

        String cmd[] = CommandStringFFmpeg.getCommandCreadVideoFromImageAndMusic(this,
                pathSound, timeLoad, pathSave);
        Log.e(TAG, "createVideo(): " + cmd.toString());

        try {
            FFmpeg.getInstance(this).execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    joinVideoFrame();
                }

                @Override
                public void onFailure(String message) {
                    showLog();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            showLog();
            e.printStackTrace();
        }
    }

    private void joinVideoFrame() {
        step = STEP_2;
        String pathSave = MyPath.getPathSaveVideo(this)
                + nameVideo + ".mp4";
        String cmd[];

        if (uriVideoFrames.isEmpty()) {
            cmd = CommandStringFFmpeg.moveVideo(this, pathSave);
        } else {
            cmd = CommandStringFFmpeg.addVideoFrame(this, myApplication.getFrameVideo(), pathSave, myApplication);
        }

        Log.e(TAG, "joinVideoFrame(): " + cmd.toString());

        try {
            FFmpeg.getInstance(this).execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    step = STEP_3;
                    isSuccess = true;

                    binding.seekbarPoint.setPoints(100);
                    myApplication.removeAllImage();
                    myApplication.initData();
                }

                @Override
                public void onFailure(String message) {
                    isSuccess = false;
                    step = STEP_3;
                    process = 100;
                    myApplication.initData();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            showLog();
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
            if (process >= 97 && step != STEP_3) {
                process = 97;
                handler.removeCallbacks(this);
                isRunEnd = true;
            } else if (process >= 97) {
                process = 100;
                isRunEnd = true;
            }
            binding.seekbarPoint.setPoints(process);
            handler.postDelayed(this, 25);
            if (process == 100) {
                showLog();
                handler.removeCallbacks(this);
                myApplication.initData();
                showVideo();
            }
        }
    };

    private void showVideo() {
        binding.btnCancel.setVisibility(View.GONE);
        if (!isShowLogRemove) {
            CreatedFileActivity.startIntentWithVideo(this, MyPath.getPathSaveVideo(this) + nameVideo + ".mp4");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        if (step != STEP_3) {
//            showAlert();
//        }
        if (step == STEP_3)
            super.onBackPressed();
    }

    private void initAlert() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.ques_cancel_video));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), getString(R.string.cancel_success), true);
            onCancelVideo();
            finish();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
            isShowLogRemove = false;
            if (step == STEP_3 && isSuccess) {
                showVideo();
            }
        });
    }

    private void showAlert() {
        isShowLogRemove = true;
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showLog() {
        if (!isShow) {
            if (isSuccess) {
                ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), getString(R.string.save_video_success), true);
            } else {
                ShowLog.ShowLog(SaveVideoActivity.this, binding.getRoot(), getString(R.string.save_video_fail), false);
                binding.btnCancel.setVisibility(View.GONE);
            }
            isShow = true;
        }
    }

    private void onCancelVideo() {
        isCancel = true;
        process = 100;
        step = STEP_3;
        isSuccess = false;
        handler.removeCallbacks(runnable);
        if (step == STEP_3) {
            String pathVideo = MyPath.getPathSaveVideo(this) + nameVideo + ".mp4";
            File file = new File(pathVideo);
            if (file.exists()) {
                file.delete();
            }
        }
        ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.stop_video), true);
        finish();
    }
}
