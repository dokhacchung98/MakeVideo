package com.khacchung.makevideo.activity;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityPlayVideoBinding;

public class PlayVideoActivity extends BaseActivity implements OnPreparedListener {
    private ActivityPlayVideoBinding binding;
    private String pathVideo;
    private static final String PATH_VIDEO = "path_video";

    public static void startIntent(Activity activity, String pathVideo) {
        Intent intent = new Intent(activity, PlayVideoActivity.class);
        intent.putExtra(PATH_VIDEO, pathVideo);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableBackButton();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_video);

        Intent intent = getIntent();
        if (intent != null) {
            pathVideo = intent.getStringExtra(PATH_VIDEO);
        }
        if (pathVideo == null || pathVideo.isEmpty()) {
            ShowLog.ShowLog(this, binding.getRoot(), "Có lỗi, vui lòng thử lại", false);
            finish();
        }

        initVideo();
    }

    private void initVideo() {
        binding.videoView.setOnPreparedListener(this);
        binding.videoView.setVideoURI(Uri.parse(pathVideo));
    }

    @Override
    public void onPrepared() {
        binding.videoView.start();
    }
}
