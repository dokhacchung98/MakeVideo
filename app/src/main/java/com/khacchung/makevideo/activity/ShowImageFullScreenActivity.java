package com.khacchung.makevideo.activity;

import androidx.databinding.DataBindingUtil;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityShowImageFullScreenBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.model.MyImageModel;


public class ShowImageFullScreenActivity extends BaseActivity implements MyClickHandler, Animator.AnimatorListener {
    private ActivityShowImageFullScreenBinding binding;
    private static final String MODEL_IMAGE = "model_image";
    private MyImageModel myImageModel;
    private boolean isShow = false;

    public static void startIntent(Activity activity, MyImageModel model) {
        Intent intent = new Intent(activity, ShowImageFullScreenActivity.class);
        intent.putExtra(MODEL_IMAGE, model);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_image_full_screen);
        binding.setHandler(this);

        Intent intent = getIntent();
        myImageModel = (MyImageModel) intent.getSerializableExtra(MODEL_IMAGE);

        if (myImageModel == null) {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            finish();
        }
        binding.setModel(myImageModel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgThumbnail:
                toogleTopBar();
                break;
            case R.id.txtBack:
                finish();
                break;
//            case R.id.btnEdit:
//                EditImageActivity.startInternt(this, myImageModel.getPathImage(), binding.getRoot(), false);
//                show();
//                finish();
//                break;
            case R.id.btnShare:
                intentShareImage(myImageModel.getPathImage());
                break;
        }
    }

    private void toogleTopBar() {
        if (isShow) {
            binding.frTop.animate()
                    .alpha(0.0f)
                    .setListener(this)
                    .setDuration(100);

        }
        binding.frTop.setVisibility(View.VISIBLE);
        binding.frTop.animate()
                .alpha(1.0f)
                .setDuration(100)
                .setListener(this);
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (isShow)
            binding.frTop.setVisibility(View.GONE);
        isShow = !isShow;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
