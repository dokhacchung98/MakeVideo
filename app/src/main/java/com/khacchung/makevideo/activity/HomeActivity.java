package com.khacchung.makevideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

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

    private Animation animThumbnail;
    private Animation animAppName;

    public static final String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

        realtimePermission(PERMISSION_LIST);

        animThumbnail = AnimationUtils.loadAnimation(this, R.anim.anim_thumbnail);
        animAppName = AnimationUtils.loadAnimation(this, R.anim.anim_app_name);

        binding.imgThumbnail.setAnimation(animThumbnail);
        binding.imgNameApp.setAnimation(animAppName);

        animAppName.start();
        animThumbnail.start();
    }

    private void writeFileOnFirstTimeOpenApp() {
        Log.e(TAG,"writeFileOnFirstTimeOpenApp() start");
        if (ContextCompat.checkSelfPermission(this, PERMISSION_LIST[1]) == PackageManager.PERMISSION_GRANTED) {
            File file = new File(MyPath.getPathFrame(this));
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.listFiles().length == 0) {
                showLoading(getString(R.string.saving_first));
                copyImage();
            }
        }

        if (ContextCompat.checkSelfPermission(this, PERMISSION_LIST[1]) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditImage:
                if (ContextCompat.checkSelfPermission(this, PERMISSION_LIST[0]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, PERMISSION_LIST[1]) == PackageManager.PERMISSION_GRANTED) {
                    ListImageEditActivity.startIntent(this);
                } else {
                    realtimePermission(PERMISSION_LIST);
                }
                break;
            case R.id.btnCreateVideo:
                if (ContextCompat.checkSelfPermission(this, PERMISSION_LIST[0]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, PERMISSION_LIST[1]) == PackageManager.PERMISSION_GRANTED) {
                    SelectImageActivity.startIntent(this);
                } else {
                    realtimePermission(PERMISSION_LIST);
                }
                break;
            case R.id.btnCreatedFile:
                if (ContextCompat.checkSelfPermission(this, PERMISSION_LIST[0]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, PERMISSION_LIST[1]) == PackageManager.PERMISSION_GRANTED) {
                    CreatedFileActivity.startIntent(this);
                } else {
                    realtimePermission(PERMISSION_LIST);
                }
                break;
            case R.id.btnOut:
                onBackPressed();
                break;
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
    }

    private void seeMore() {
        ShowDialogInfo();
    }

    private void shareApp() {
        intentShareApp(binding.getRoot());
    }

    private void feedback() {
        ShowDialogSendFeedback();
    }

    @Override
    public void onClickWithData(View view, Object value) {


    }

    private void copyImage() {
        Log.e(TAG,"copyImage() start");
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
        Log.e(TAG,"copySound() start");
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

    @Override
    public void onBackPressed() {
        showDialogAlertExit();
    }

    private void ShowDialogInfo() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);
        TextView textView = dialog.findViewById(R.id.txtVersion);
        try {
            textView.setText(getString(R.string.version) + getPackageInfo().versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener((v) -> dialog.cancel());
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    private void ShowDialogSendFeedback() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_send_feedback);
        dialog.setCancelable(false);
        EditText txtContent = dialog.findViewById(R.id.edt_content);
        Button btnSend = dialog.findViewById(R.id.btnOk);
        btnSend.setOnClickListener((v) -> {
            String title = "";
            try {
                title = getString(R.string.feedback) + "-"
                        + getString(R.string.app_name) + "-Version:"
                        + getPackageInfo().versionName;
                Log.e(TAG, "ShowDialogSendFeedback() title: " + title);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String content = txtContent.getText().toString().trim();
            if (content.isEmpty()) {
                txtContent.setError(getString(R.string.error_input));
                return;
            }
            intentSendEmail(title, content);
            dialog.cancel();
        });

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener((v) -> dialog.cancel());
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        writeFileOnFirstTimeOpenApp();
    }
}