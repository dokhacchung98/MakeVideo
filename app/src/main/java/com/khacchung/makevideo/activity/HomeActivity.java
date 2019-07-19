package com.khacchung.makevideo.activity;

import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
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

        myApplication = MyApplication.getInstance();

        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File file = new File(MyPath.getPathFrame(this));
            if (!file.exists()) {
                file.mkdirs();
                if (file.listFiles().length == 0) {
                    showLoading("Đang lưu trữ dữ liệu, vui lòng đợi...");
                    copyAssets();
                }
            }
        }

        initMyApplication();
    }

    private void initMyApplication() {
        myApplication.initData();
    }

    @Override
    public void onClick(View view) {
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
        }
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files;
        try {
            files = assetManager.list("frames");
        } catch (IOException e) {
            Log.e(TAG, "Failed to get asset file list.", e);
            hideLoading();
            ShowLog.ShowLog(this, binding.getRoot(), "Có lỗi, vui lòng thử lại", false);
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
                ShowLog.ShowLog(this, binding.getRoot(), "Có lỗi, vui lòng thử lại", false);
                return;
            }
        }
        hideLoading();
        ShowLog.ShowLog(this, binding.getRoot(), "Lưu trữ thành công", true);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
