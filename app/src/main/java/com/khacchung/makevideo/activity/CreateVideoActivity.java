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
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
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
import com.khacchung.makevideo.fragment.TimerFramgent;
import com.khacchung.makevideo.handler.CreatedListener;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.model.MyFrameModel;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.model.MyTimerModel;
import com.khacchung.makevideo.service.CreateVideoService;

import java.io.File;
import java.util.ArrayList;

public class CreateVideoActivity extends BaseActivity implements CreatedListener, MyClickHandler, SeekBar.OnSeekBarChangeListener {
    public static final int REQUEST_CUT_SOUND = 86;
    private String TAG = CreateVideoActivity.class.getName();

    private ActivityCreateVideoBinding binding;
    private MyApplication myApplication;
    private ArrayList<MyImageModel> listImage;
    private ArrayList<MyMusicModel> listMusic;
    private ArrayList<THEMES> listEffect;
    private ArrayList<MyTimerModel> listTimer;
    private ArrayList<MyFrameModel> listFrame;

    private ListImageFramgent listImageFramgent;
    private ListEffectsFramgent listEffectsFramgent;
    private ListMusicFramgent listMusicFramgent;
    private ListImageFramesFramgent listImageFramesFramgent;
    private TimerFramgent timerFramgent;
    private Handler handler = new Handler();

    private MediaPlayer mediaPlayer;

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

        initFragment();

        initViewPager();

        onChangedVideoFrame();

//        binding.tabLayout.setupWithViewPager(binding.viewPager);
//        binding.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager));
//        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.viewPager.setEnabled(false);
        binding.setHandler(this);
        binding.seekbarTime.setOnSeekBarChangeListener(this);

        renderVideo();
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
    }

    private void initViewPager() {
        FragmentManager manager = getSupportFragmentManager();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this, manager, listImageFramgent,
                listEffectsFramgent, listMusicFramgent, listImageFramesFramgent, timerFramgent);
        binding.setAdapter(myPagerAdapter);
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
            listImage.remove(0);
            myApplication.setListIamge(listImage);

            SaveVideoActivity.startIntent(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void renderVideo() {
        CreateVideoService.stopService(this);
        CreateVideoService.startService(this);
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
    }

    private int currentFrame = 0;
    private int sizeMaxFrame = 0;

    private int currentTime = 0;
    private int totalTime = 0;

    private boolean isPlaying = true;

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
//        sizeMaxFrame = numberOfFrames;
        hideLoading();
//        runOnUiThread(() -> initVideo());
    }

    @Override
    public void onUpdate(int size) {
        binding.seekbarTime.setSecondaryProgress(size);
    }

    @Override
    public void onStartCreateVideo() {
        Log.e(TAG, "onStartCreateVideo()");
        sizeMaxFrame = (listImage.size() - 2) * 30;
        runOnUiThread(() -> initVideo());
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
        isPlaying = false;
        handler.removeCallbacks(runnable);
        pauseMusic();
    }

    private void playVideo() {
        hideLoading();
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
            if (isPlaying) {
                pauseVideo();
            } else {
                playVideo();
            }
        }
    }

    @Override
    public void onClickWithData(View view, Object value) {

    }

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
        }
    }

    private void seekMusic(int seconds) {
        if (mediaPlayer != null) {
            if (seconds <= (mediaPlayer.getDuration() / 1000)) {
                mediaPlayer.seekTo(seconds);
            } else {
                mediaPlayer.seekTo(mediaPlayer.getDuration() / 1000);
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
}