package com.khacchung.makevideo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.ActivitySplashScreenBinding;
import com.khacchung.makevideo.handler.MyClickHandler;

public class SplashScreenActivity extends AppCompatActivity implements MyClickHandler {
    private static final String TAG = SplashScreenActivity.class.getName();
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        binding.setHandler(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
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
