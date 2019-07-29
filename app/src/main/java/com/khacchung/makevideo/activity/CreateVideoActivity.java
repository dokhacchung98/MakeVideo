package com.khacchung.makevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.MyPagerAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.base.ShowLog;
import com.khacchung.makevideo.databinding.ActivityCreateVideoBinding;
import com.khacchung.makevideo.extention.GetFileFromURI;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.fragment.ListEffectsFramgent;
import com.khacchung.makevideo.fragment.ListImageFramesFramgent;
import com.khacchung.makevideo.fragment.ListImageFramgent;
import com.khacchung.makevideo.fragment.ListMusicFramgent;
import com.khacchung.makevideo.fragment.QualityFramgent;
import com.khacchung.makevideo.fragment.TimerFramgent;
import com.khacchung.makevideo.handler.CreatedListener;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.model.MyFrameModel;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.model.MyQualityModel;
import com.khacchung.makevideo.model.MyTimerModel;
import com.khacchung.makevideo.model.MyVector;
import com.khacchung.makevideo.service.CreateVideoService;

import java.io.File;
import java.util.ArrayList;

public class CreateVideoActivity extends BaseActivity implements CreatedListener, MyClickHandler, SeekBar.OnSeekBarChangeListener, ViewPager.OnPageChangeListener {
    public static final int REQUEST_CUT_SOUND = 86;
    private String TAG = CreateVideoActivity.class.getName();

    private ActivityCreateVideoBinding binding;
    private MyApplication myApplication;
    private ArrayList<MyImageModel> listImage;
    private ArrayList<MyMusicModel> listMusic;
    private ArrayList<THEMES> listEffect;
    private ArrayList<MyTimerModel> listTimer;
    private ArrayList<MyFrameModel> listFrame;
    private ArrayList<MyQualityModel> listQuality;

    private ListImageFramgent listImageFramgent;
    private ListEffectsFramgent listEffectsFramgent;
    private ListMusicFramgent listMusicFramgent;
    private ListImageFramesFramgent listImageFramesFramgent;
    private TimerFramgent timerFramgent;
    private QualityFramgent qualityFramgent;
    private Handler handler = new Handler();

    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerTemp;

    private boolean isLoadingSuccess = false;

    private boolean isStart = true;

    private int currentFrame = 0;
    private int sizeMaxFrame = 0;

    private int currentTime = 0;
    private int totalTime = 0;

    private boolean isPlaying = true;
    private boolean isPauseVideo = true;
    private boolean isLoading = true;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, CreateVideoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        setTitleToolbar(getString(R.string.create_video));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_video);

        MyVector vector = getWidthAndHeight();
        int myW;
        int myH;
        if (vector.getHeight() > vector.getWidth()) {
            myW = vector.getWidth();
            myH = (int) ((float) myW / 1.778);
        } else {
            myH = (int) ((float) vector.getHeight() / 2) - 100;
            myW = (int) ((float) myH * 1.778);
        }

        binding.frVideo.setLayoutParams(new FrameLayout.LayoutParams(
                myW,
                myH
        ));

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation_anim_loading);
        binding.imgLoading.setAnimation(animation);
        animation.start();

        myApplication = MyApplication.getInstance();
        myApplication.registerListener(this);
        myApplication.setEnd(true);

        listImage = new ArrayList<>();
        listImage.add(new MyImageModel());
        listImage.addAll(myApplication.getListIamge());

        if (listImage.size() < 3) {
            ShowLog.ShowLog(this, binding.getRoot(), getString(R.string.errror), false);
            finish();
        }

        getListMusic();

        initEffects();
        initTimer();
        initFrame();
        initQuality();

        initFragment();

        initViewPager();

        onChangedVideoFrame();

        binding.viewPager.setEnabled(false);
        binding.setHandler(this);
        binding.seekbarTime.setOnSeekBarChangeListener(this);
        binding.pagerTab.setTextColor(getResources().getColor(R.color.colorWhite));
        binding.pagerTab.setTabIndicatorColorResource(R.color.colorWhite);

        renderVideo();
    }

    private void initQuality() {
        if (listQuality == null) {
            listQuality = new ArrayList<>();
        }
        listQuality.clear();

        String name = myApplication.getQuality().getName();

        listQuality.add(new MyQualityModel(MyApplication.PX_240P, false));
        listQuality.add(new MyQualityModel(MyApplication.PX_360P, false));
        listQuality.add(new MyQualityModel(MyApplication.PX_480P, false));
        listQuality.add(new MyQualityModel(MyApplication.PX_720P, false));

        for (MyQualityModel model : listQuality) {
            if (model.getQuality().getName().equals(name)) {
                model.setSelected(true);
                break;
            }
        }
    }

    private void initTimer() {
        if (listTimer == null) {
            listTimer = new ArrayList<>();
        }
        listTimer.clear();
        int i = (int) myApplication.getTimeLoad();

        listTimer.add(new MyTimerModel(1, false));
        listTimer.add(new MyTimerModel(2, false));
        listTimer.add(new MyTimerModel(3, false));
        listTimer.add(new MyTimerModel(4, false));
        listTimer.add(new MyTimerModel(5, false));

        for (MyTimerModel model : listTimer) {
            if (model.getTime() == i) {
                model.setSelected(true);
                break;
            }
        }
    }

    private void initFrame() {
        if (listFrame == null) {
            listFrame = new ArrayList<>();
        }
        listFrame.clear();

        listFrame.add(new MyFrameModel(""));

        File file[] = new File(MyPath.getPathFrame(this)).listFiles();
        for (File f : file) {
            MyFrameModel tmp = new MyFrameModel();
            tmp.setPathFrame(f.getAbsolutePath());
            tmp.setSelected(false);
            listFrame.add(tmp);
        }
    }

    private void initEffects() {
        if (listEffect == null) {
            listEffect = new ArrayList<>();
        }
        listEffect.clear();
        for (THEMES item : THEMES.values()) {
            listEffect.add(THEMES.valueOf(item.name()));
        }
    }

    private void initFragment() {
        listImageFramgent = new ListImageFramgent(this, listImage, myApplication);
        listEffectsFramgent = new ListEffectsFramgent(this, listEffect, myApplication);
        listMusicFramgent = new ListMusicFramgent(this, listMusic, myApplication);
        listImageFramesFramgent = new ListImageFramesFramgent(this, listFrame, myApplication);
        timerFramgent = new TimerFramgent(this, myApplication, listTimer);
        qualityFramgent = new QualityFramgent(this, myApplication, listQuality);
    }

    private void initViewPager() {
        FragmentManager manager = getSupportFragmentManager();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this, manager, listImageFramgent,
                listEffectsFramgent, listMusicFramgent, listImageFramesFramgent, timerFramgent, qualityFramgent);
        binding.setAdapter(myPagerAdapter);
        binding.viewPager.setOnPageChangeListener(this);
    }

    private void getListMusic() {
        if (listMusic == null) {
            listMusic = new ArrayList<>();
        }
        listMusic.clear();
        File directory = new File(MyPath.getPathSound(this));
        if (directory.exists()) {
            File file[] = directory.listFiles();
            for (File f : file) {
                listMusic.add(new MyMusicModel(f.getAbsolutePath(), f.getName()));
            }
        }


        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        listMusic.addAll(GetFileFromURI.getAllMusicFromURI(this, uri.toString()));

        if (!myApplication.getPathMusic().isEmpty()) {
            int in = listMusic.indexOf(myApplication.getMyMusicModel());
            if (in != -1) {
                listMusic.get(in).setSelected(true);
            }
        } else {
            if (listMusic.size() > 0) {
                listMusic.get(0).setSelected(true);
                myApplication.setMyMusicModel(listMusic.get(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (!isLoadingSuccess) {
                ShowLog.ShowLog(CreateVideoActivity.this, binding.getRoot(), getString(R.string.errror), false);
                return false;
            }
            listImage.remove(0);
            myApplication.setListIamge(listImage);

            SaveVideoActivity.startIntent(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void renderVideo() {
        stopVideo();
        CreateVideoService.stopService(this);
        CreateVideoService.startService(this);
        isLoadingSuccess = false;
        showMess();
    }

    private void initVideo() {
        binding.txtCurrentTime.setText(String.format("%02d:%02d", 0, 0));
        handler.removeCallbacks(runnable);

        binding.seekbarTime.setMax(sizeMaxFrame);

        currentFrame = 0;
        changeTotalTime();

        changeCurrentTimeByCurrentFrame();

        initMusic();
        playVideo();

        compareTimeSoundAndVideo();
    }

    private void setImage() {
        String path = MyPath.getPathTemp(this) + String.format("img" + "%04d.jpg", currentFrame);
        currentFrame++;
        if (currentFrame >= sizeMaxFrame) {
            currentFrame = 0;
            handler.removeCallbacks(runnable);
            pauseVideo();
            restartMusic();
        }

        changeCurrentTimeByCurrentFrame();

        binding.imgThumbnail.setImageURI(Uri.parse(path));
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, (int) (40 * myApplication.getTimeLoad()));
            setImage();
        }
    };

    @Override
    public void onSuccess(int numberOfFrames) {
        Log.e(TAG, "onSuccess Listener service: " + numberOfFrames);
        hideLoading();
        closeLoading();
        isLoadingSuccess = true;
    }

    @Override
    public void onUpdate(int size) {
        binding.seekbarTime.setSecondaryProgress(size);
        if (!isStart && !isPlaying && size - currentPause >= 20 && !isPauseVideo) {
            runOnUiThread(() -> playVideo());
            closeLoading();
        }
    }

    @Override
    public void onStartCreateVideo() {
        Log.e(TAG, "onStartCreateVideo()");
        sizeMaxFrame = (listImage.size() - 2) * 30;
        isStart = false;
        runOnUiThread(() -> initVideo());
    }

    @Override
    public void onFaild(Exception e) {
        ShowLog.ShowLog(this, binding.getRoot(),
                getString(R.string.errror) + " : " + e.getMessage(), false);
        SelectImageActivity.startIntent(this);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MoveIndexActivity.startIntent(this);
    }

    @Override
    public void onChangedTimeFrame() {
        Log.e(TAG, "onChangedTimeFrame(): " + myApplication.getTimeLoad());
        changeTotalTime();
        stopVideo();
        playVideo();
        compareTimeSoundAndVideo();
    }

    @Override
    public void onChangedEffect() {
        Log.e(TAG, "onChangedEffect(): " + myApplication.getSeletedTheme().getName());
        renderVideo();
    }

    @Override
    public void onChangedMusic() {
        Log.e(TAG, "onChangedMusic(): " + myApplication.getMyMusicModel().getNameMusic());
        stopVideo();
        initMusic();
        playVideo();
        compareTimeSoundAndVideo();
    }

    @Override
    public void onChangedQuality() {
        Log.e(TAG, "onChangedQuality(): " + myApplication.getQuality().getName());
        renderVideo();
    }

    @Override
    public void onChangedVideoFrame() {
        Log.e(TAG, "onChangedVideoFrame()");
        String pathFrame = myApplication.getFrameVideo();
        if (!pathFrame.isEmpty()) {
            binding.imgFrame.setImageURI(Uri.parse(pathFrame));
        } else {
            binding.imgFrame.setImageResource(0);
        }

        stopVideo();
        playVideo();
    }

    private void pauseVideo() {
        binding.frPlay.setVisibility(View.VISIBLE);
        binding.frLoading.setVisibility(View.GONE);
        isPlaying = false;
        handler.removeCallbacks(runnable);
        pauseMusic();
    }

    private void playVideo() {
        hideLoading();
        isPauseVideo = false;
        binding.frPlay.setVisibility(View.GONE);
        isPlaying = true;
        playMusic();
        handler.post(runnable);
    }

    private void stopVideo() {
        binding.frPlay.setVisibility(View.VISIBLE);
        currentFrame = 0;
        handler.removeCallbacks(runnable);
        isPlaying = false;
        restartMusic();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fr_video) {
            if (!isPlaying) {
                playVideo();
            } else {
                isPauseVideo = true;
                pauseVideo();
            }
        }
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

    private int currentPause = 0;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress >= 0 && progress <= sizeMaxFrame && fromUser) {
            if (progress > seekBar.getSecondaryProgress()) {
                currentFrame = seekBar.getSecondaryProgress() - 1;
            } else {
                currentFrame = progress;
            }
            int tmp = (int) (((float) currentFrame / sizeMaxFrame) * totalTime * 1000);
            seekMusic(tmp);
            setImage();
        }
        if (!fromUser) {
            if (progress >= seekBar.getSecondaryProgress()) {
                pauseVideo();
                currentPause = progress;
                showLoading();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void changeCurrentTimeByCurrentFrame() {
        currentTime = (int) (((float) currentFrame / sizeMaxFrame) * totalTime);
        binding.seekbarTime.setProgress(currentFrame);
        int mm = currentTime / 60;
        int ss = currentTime % 60;
        binding.txtCurrentTime.setText(String.format("%02d:%02d", mm, ss));
    }

    private void changeTotalTime() {
        totalTime = (int) ((sizeMaxFrame) * myApplication.getTimeLoad()) / 30;
        int mm = totalTime / 60;
        int ss = totalTime % 60;
        binding.txtTotalTime.setText(String.format("%02d:%02d", mm, ss));
    }

    @Override
    public void onChangedImage() {
        renderVideo();
    }

    private void showMess() {
        showLoading(getString(R.string.apply_in_video));
    }

    private void initMusic() {
        if (myApplication.getMyMusicModel() != null) {
            String pathMusic;
            if (myApplication.getPathMusic().isEmpty()) {
                pathMusic = myApplication.getMyMusicModel().getPathMusic();
            } else {
                pathMusic = myApplication.getPathMusic();
            }
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, Uri.parse(pathMusic));
            mediaPlayer.setLooping(false);
        }
    }

    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void restartMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);

            if (isPlaying && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            } else if (isPlaying && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    private void seekMusic(int seconds) {
        if (mediaPlayer != null) {
            boolean isEnd;
            if (seconds <= (mediaPlayer.getDuration())) {
                mediaPlayer.seekTo(seconds);
                isEnd = false;
            } else {
                mediaPlayer.seekTo(mediaPlayer.getDuration());
                isEnd = true;
            }
            if (isPlaying && !mediaPlayer.isPlaying() && !isEnd) {
                mediaPlayer.start();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EditImageActivity.REQUEST_CODE_EDIT_IMAGE && data != null) {
            String oldImg = data.getStringExtra(EditImageActivity.URI_IMAGE);
            String newImg = data.getStringExtra(EditImageActivity.PATH_IMAGE_NEW);
            int index = data.getIntExtra(EditImageActivity.INDEX_IMAGE, -1);
            Log.e(TAG, "onActivityResult(): old image: " + oldImg + ", new image: " + newImg + ", index: " + index);

            if (index >= 0 && index < listImage.size()) {
                File file = new File(newImg);
                MyImageModel model = new MyImageModel();
                model.setPathImage(file.getAbsolutePath());
                model.setPathParent(file.getParent());

                listImage.set(index, model);
            } else {
                for (MyImageModel model : listImage) {
                    if (model.getPathImage() != null) {
                        if (model.getPathImage().equals(oldImg)) {
                            File file = new File(newImg);
                            if (file.exists()) {
                                model.setPathImage(file.getAbsolutePath());
                                model.setPathParent(file.getParent());
                            }
                            break;
                        }
                    }
                }
            }
            if (listImageFramgent != null) {
                listImageFramgent.updateListImage(listImage);
            }
            onChangedImage();
        } else if (requestCode == Activity.RESULT_OK && data != null && resultCode == REQUEST_CUT_SOUND) {
            String pathSound = data.getStringExtra(CutSoundActivity.SOUND);
            if (myApplication.getPathMusic().equals(pathSound)) {
                onChangedMusic();
            }
        }
    }

    private void compareTimeSoundAndVideo() {
        String pathApp = myApplication.getPathMusic();
        MyMusicModel model = myApplication.getMyMusicModel();
        String myPath = "";

        if (pathApp == null || pathApp.isEmpty()) {
            if (model != null) {
                myPath = model.getPathMusic();
            }
        } else {
            myPath = pathApp;
        }

        if (!myPath.isEmpty()) {
            mediaPlayerTemp = MediaPlayer.create(this, Uri.parse(myPath));
            int sec = mediaPlayerTemp.getDuration() / 1000;
            if (sec < totalTime) {
                myApplication.setAlertSound(true);
            } else {
                myApplication.setAlertSound(false);
            }
            Log.e(TAG, "compareTimeSoundAndVideo(): " + myApplication.isAlertSound());
            if (listMusicFramgent != null) {
                listMusicFramgent.openOrCloseAlert();
            }
        }
        if (mediaPlayerTemp != null)
            mediaPlayerTemp.release();
        mediaPlayerTemp = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initVideo();
    }

    @Override
    public void finish() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                binding.pagerTab.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                break;
            case 1:
                binding.pagerTab.setBackgroundColor(getResources().getColor(R.color.color_material_6));
                break;
            case 2:
                binding.pagerTab.setBackgroundColor(getResources().getColor(R.color.color_material_10));
                break;
            case 3:
                binding.pagerTab.setBackgroundColor(getResources().getColor(R.color.color_material_19));
                break;
            case 4:
                binding.pagerTab.setBackgroundColor(getResources().getColor(R.color.color_material_29));
                break;
            case 5:
                binding.pagerTab.setBackgroundColor(getResources().getColor(R.color.color_material_1));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showLoading() {
        isLoading = true;
        runOnUiThread(() -> {
            binding.frLoading.setVisibility(View.VISIBLE);
            binding.frPlay.setVisibility(View.GONE);
        });
    }

    private void closeLoading() {
        isLoading = false;
        runOnUiThread(() -> binding.frLoading.setVisibility(View.GONE));
    }
}