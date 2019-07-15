package com.khacchung.makevideo.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.ActivityPermissionBinding;
import com.khacchung.makevideo.handler.MyClickHandler;

public class PermissionActivity extends BaseActivity implements MyClickHandler {

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, PermissionActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        ActivityPermissionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_permission);
        binding.setHandler(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        gotoHomeActivity();
    }

    private void gotoHomeActivity() {
        HomeActivity.startIntent(this);
    }

    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permisson : BaseActivity.PERMISSION_LIST) {
                if (checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(BaseActivity.PERMISSION_LIST, 0);
                    return;
                }
            }
        }
        gotoHomeActivity();
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }
}
