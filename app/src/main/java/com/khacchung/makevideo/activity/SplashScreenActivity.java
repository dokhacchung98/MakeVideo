package com.khacchung.makevideo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.ActivitySplashScreenBinding;
import com.khacchung.makevideo.handler.MyClickHandler;

public class SplashScreenActivity extends BaseActivity implements MyClickHandler {
    private static final String TAG = SplashScreenActivity.class.getName();
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        binding.setHandler(this);
        handler = new Handler();
        handler.postDelayed(runnable, 3000);
    }

    private Runnable runnable = this::intentHome;

    @Override
    public void onClick(View view) {
        intentHome();
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    private void intentHome() {
        handler.removeCallbacks(runnable);
        Log.e(TAG, "intentHome()");
        HomeActivity.startIntent(this);
        finish();
    }
}
