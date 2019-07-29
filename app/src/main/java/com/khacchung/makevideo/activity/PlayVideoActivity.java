package com.khacchung.makevideo.activity;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityPlayVideoBinding;

import java.io.File;

public class PlayVideoActivity extends BaseActivity {
    private ActivityPlayVideoBinding binding;
    private String pathVideo;
    private static final String PATH_VIDEO = "path_video";

    public static void startIntent(Activity activity, String pathVideo) {
        Intent intent = new Intent(activity, PlayVideoActivity.class);
        intent.putExtra(PATH_VIDEO, pathVideo);
        activity.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            intentShareVideo(pathVideo);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableBackButton();
        setTitleToolbar(getString(R.string.watching));
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_video);

        Intent intent = getIntent();
        if (intent != null) {
            pathVideo = intent.getStringExtra(PATH_VIDEO);
        }
        if (pathVideo == null || pathVideo.isEmpty()) {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            finish();
        }

        initVideo();
    }

    private void initVideo() {
        File file = new File(pathVideo);
        if (file.exists()) {
            binding.fullscreenVideoView
                    .videoFile(file)
                    .enableAutoStart()
                    .addSeekBackwardButton()
                    .addSeekForwardButton();
        } else {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            finish();
        }
    }
}
