package com.khacchung.makevideo.base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;

import com.khacchung.makevideo.BuildConfig;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.activity.PermissionActivity;
import com.khacchung.makevideo.handler.ChangedListener;
import com.khacchung.makevideo.handler.MyCallBack;
import com.khacchung.makevideo.model.MyVector;

import java.io.File;

public class BaseActivity extends AppCompatActivity implements ChangedListener {

    public static final int READ_WRITE_STORAGE = 52;
    private static final String TAG = BaseActivity.class.getName();
    private ProgressDialog mProgressDialog;
    public static final String[] PERMISSION_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ProgressDialog dialog;

    public boolean requestPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    READ_WRITE_STORAGE);
        }
        return isGranted;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permisson : PERMISSION_LIST) {
                if (checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(PERMISSION_LIST, 0);
                    return;
                }
            }
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.wait));
        dialog.setCancelable(false);
    }

    protected void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    protected void setTitleToolbar(String title) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                PermissionActivity.startIntent(this);
                return;
            }
        }
    }

    protected MyVector getWidthAndHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels + getNavigationBarHeight();
        int width = displayMetrics.widthPixels;

        return new MyVector(width, height);
    }

    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    protected void enableBackButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
            mProgressDialog.dismiss();
        }
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    protected void close() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        close();
    }

    public void showDialogDeleteFile(String pathFile, View view, MyCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert));
        builder.setMessage(getString(R.string.ques_delete));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            File file = new File(pathFile);
            if (file.exists()) {
                file.delete();
            }
            ShowLog.ShowLog(this, view, getString(R.string.delete_suc), true);
            callBack.onSuccess();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void intentShareImage(String path) {
        MediaScannerConnection.scanFile(this, new String[]{path},

                null, (path1, uri) -> {
                    Intent shareIntent = new Intent(
                            Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(Intent.createChooser(shareIntent,
                            "Share Image..."));

                });
    }

    protected void intentShareVideo(String path) {
        MediaScannerConnection.scanFile(this, new String[]{path},

                null, (path1, uri) -> {
                    Intent shareIntent = new Intent(
                            Intent.ACTION_SEND);
                    shareIntent.setType("video/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(Intent.createChooser(shareIntent,
                            "Share Video..."));

                });
    }

    protected void intentSendEmail(String title, String content) {
        ShareCompat.IntentBuilder.from(this)
                .setType("message/rfc822")
                .addEmailTo(getString(R.string.email_send))
                .setSubject(title)
                .setText(content)
                .setChooserTitle(getString(R.string.email_via))
                .startChooser();
    }

    protected void intentShareApp(View view) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = getString(R.string.app_name);
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Choose one"));
        } catch (Exception e) {
            ShowLog.ShowLog(this, view, getString(R.string.errror), false);
        }
    }

    protected PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        return info;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onChangedEffect() {

    }

    @Override
    public void onChangedMusic() {

    }

    @Override
    public void onChangedVideoFrame() {

    }

    @Override
    public void onChangedTimeFrame() {

    }

    @Override
    public void onChangedImage() {

    }

    @Override
    public void onChangedQuality() {

    }
}