package com.khacchung.makevideo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityHomeBinding;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.handler.MyClickHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HomeActivity extends BaseActivity implements MyClickHandler {
    private ActivityHomeBinding binding;
    private static final String TAG = HomeActivity.class.getName();
    private MyApplication myApplication;

    private Handler handler = new Handler();

    private boolean isPress = true;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setHandler(this);

        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File file = new File(MyPath.getPathFrame(this));
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.listFiles().length == 0) {
                showLoading(getString(R.string.saving_first));
                copyImage();
            }
        }

        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File file = new File(MyPath.getPathSound(this));
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.listFiles().length == 0) {
                showLoading(getString(R.string.saving_second));
                copySound();
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isPress = true;
            handler.removeCallbacks(this);
        }
    };

    @Override
    public void onClick(View view) {
        if (isPress) {
            switch (view.getId()) {
                case R.id.btnEditImage:
                    ListImageEditActivity.startIntent(this);
                    break;
                case R.id.btnCreateVideo:
                    SelectImageActivity.startIntent(this);
                    break;
                case R.id.btnCreatedFile:
                    CreatedFileActivity.startIntent(this);
                    break;
                case R.id.btnOut:
                    showDialogAlertExit();
                case R.id.btnFeedBack:
                    feedback();
                    break;
                case R.id.btnShare:
                    shareApp();
                    break;
                case R.id.btnMore:
                    seeMore();
                    break;
            }
            isPress = false;
            handler.postDelayed(runnable, 100);
        }
    }

    private void seeMore() {
        //todo: see more
    }

    private void shareApp() {
        //todo: share application
    }

    private void feedback() {
        //todo: feedback
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    private void copyImage() {
        AssetManager assetManager = getAssets();
        String[] files;
        try {
            files = assetManager.list("frames");
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
            hideLoading();
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            return;
        }

        assert files != null;
        for (String filename : files) {
            InputStream in;
            OutputStream out;
            try {
                String pathSave = MyPath.getPathFrame(this);
                in = assetManager.open("frames/" + filename);
                File outFile = new File(pathSave, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to copy asset file: " + filename, e);
                hideLoading();
                ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
                return;
            }
        }
        hideLoading();
        ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.save_success), true);
    }

    private void copySound() {
        AssetManager assetManager = getAssets();
        String[] files;
        try {
            files = assetManager.list("sound");
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
            hideLoading();
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            return;
        }

        assert files != null;
        for (String filename : files) {
            InputStream in;
            OutputStream out;
            try {
                String pathSave = MyPath.getPathSound(this);
                in = assetManager.open("sound/" + filename);
                File outFile = new File(pathSave, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to copy asset file: " + filename, e);
                hideLoading();
                ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
                return;
            }
        }
        hideLoading();
        ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.save_success), true);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void showDialogAlertExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.alert_exit));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            finish();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}