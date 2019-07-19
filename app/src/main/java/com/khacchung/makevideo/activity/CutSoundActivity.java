package com.khacchung.makevideo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityCutSoundBinding;
import com.khacchung.makevideo.extention.CommandStringFFmpeg;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.handler.MyClickHandler;

import java.io.File;

import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler;
import nl.bravobit.ffmpeg.FFmpeg;
import nl.bravobit.ffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class CutSoundActivity extends BaseActivity implements OnRangeChangedListener, MyClickHandler, MediaPlayer.OnCompletionListener {
    private static final String TAG = CutSoundActivity.class.getName();
    private ActivityCutSoundBinding binding;
    private static final String PATH_SOUND = "path_sound";
    public static final String SOUND = "sound";
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerTemp;
    private String pathSound;
    private String currentSound;
    private int startSecond;
    private int endSecond;

    private boolean isChange = false;
    private MyApplication myApplication;
    private boolean isSave = false;
    private int totalSecond;

    public static final void startIntent(Activity activity, String uriSound) {
        Intent intent = new Intent(activity, CutSoundActivity.class);
        intent.putExtra(PATH_SOUND, uriSound);
        activity.startActivityForResult(intent, CreateVideoActivity.REQUEST_CUT_SOUND);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        setTitle("Cắt Nhạc");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cut_sound);

        Intent intent = getIntent();
        pathSound = intent.getStringExtra(PATH_SOUND);
        if (pathSound == null || pathSound.isEmpty()) {
            finish();
        }

        currentSound = pathSound;
        binding.setHandler(this);

        myApplication = MyApplication.getInstance();
        binding.sbRange.setIndicatorTextDecimalFormat("0");
        binding.sbRange.setOnRangeChangedListener(this);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation_anim);
        binding.imgThumbnail.setAnimation(animation);
        animation.start();

        File file = new File(pathSound);
        binding.txtName.setText(file.getName());

        initSound();
    }

    private void applySound() {
        cutSound();
        showLoading("Đang áp dụng, vui lòng chờ...");
    }

    private void initSound() {
        if (mediaPlayerTemp == null) {
            mediaPlayerTemp = MediaPlayer.create(this, Uri.parse(pathSound));
            totalSecond = mediaPlayerTemp.getDuration() / 1000;
            binding.sbRange.setRange(0, totalSecond);
            binding.sbRange.setProgress(0, totalSecond);
            Log.e(TAG, "initSound() totalSecond: " + totalSecond);
        }
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(currentSound));
            mediaPlayer.setOnCompletionListener(this);
        }

        startSecond = binding.sbRange.getProgressLeft();
        endSecond = binding.sbRange.getProgressRight();

        startSound();
    }

    private void startSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            binding.imgPlay.setImageResource(R.drawable.ic_button_pause);
        }
    }

    private void pauseSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            binding.imgPlay.setImageResource(R.drawable.ic_play_button_1);
        }
    }

    private void restartSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        initSound();
    }

    private void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            binding.imgPlay.setImageResource(R.drawable.ic_play_button_1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            myApplication.setPathMusic(currentSound);
            isSave = true;
            ShowLog.ShowLog(this, binding.getRoot(), "Đã áp dụng nhạc vào video", true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
        if (isFromUser) {
            startSecond = (int) leftValue;
            endSecond = (int) rightValue;

            stopSound();
        }
    }

    @Override
    public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
    }

    @Override
    public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
    }

    private void cutSound() {
        String cmd[] = CommandStringFFmpeg.cutSound(this, pathSound, startSecond, endSecond - startSecond);
        try {
            FFmpeg.getInstance(this).execute(cmd, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    currentSound = MyPath.getPathTempSound(CutSoundActivity.this) + MyPath.NAME_SOUND_TEMP;
                    initSound();
                    hideLoading();
                    isChange = true;
                }

                @Override
                public void onFailure(String message) {
                    ShowLog.ShowLog(CutSoundActivity.this, binding.getRoot(), "Có lỗi, vui lòng thử lại", false);
                    hideLoading();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.e(TAG, "onClick() pause sound");
            pauseSound();
        } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            Log.e(TAG, "onClick() start sound");
            startSound();
        } else if (mediaPlayer == null) {
            Log.e(TAG, "onClick() apply sound");
            applySound();
        }
    }

    @Override
    public void onClickWithData(View view, Object value) {
    }

    @Override
    public void finish() {
        if (isSave) {
            Intent intent = new Intent();
            intent.putExtra(SOUND, currentSound);
            setResult(Activity.RESULT_OK, intent);
            if (mediaPlayerTemp != null) {
                mediaPlayerTemp.release();
                mediaPlayerTemp = null;
            }
        }
        if (isChange && !isSave) {
            showDialog();
        }
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initSound();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chú ý");
        builder.setMessage("Bạn có muốn áp dụng file cắt nhạc này làm nhạc nền không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", (dialogInterface, i) -> {
            ShowLog.ShowLog(CutSoundActivity.this, binding.getRoot(), "Đã áp dụng", true);
            myApplication.setPathMusic(currentSound);
            isSave = true;
            finish();
        });
        builder.setNegativeButton("Hủy", (dialogInterface, i) -> {
            ShowLog.ShowLog(CutSoundActivity.this, binding.getRoot(), "Đã áp dụng", true);
            isChange = false;
            finish();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            binding.imgPlay.setImageResource(R.drawable.ic_play_button_1);
        }
    }
}