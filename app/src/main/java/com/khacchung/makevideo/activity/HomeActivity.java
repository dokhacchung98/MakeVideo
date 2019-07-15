package com.khacchung.makevideo.activity;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.ActivityHomeBinding;
import com.khacchung.makevideo.handler.MyClickHandler;

public class HomeActivity extends BaseActivity implements MyClickHandler {
    private ActivityHomeBinding binding;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setHandler(this);
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
}
