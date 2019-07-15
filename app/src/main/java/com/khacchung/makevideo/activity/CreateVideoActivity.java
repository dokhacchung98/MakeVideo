package com.khacchung.makevideo.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

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
import com.khacchung.makevideo.fragment.ListEffectsFramgent;
import com.khacchung.makevideo.fragment.ListImageFramesFramgent;
import com.khacchung.makevideo.fragment.ListImageFramgent;
import com.khacchung.makevideo.fragment.ListMusicFramgent;
import com.khacchung.makevideo.fragment.TimerFramgent;
import com.khacchung.makevideo.mask.THEMES;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.model.MyTimerModel;
import com.khacchung.makevideo.service.CreateVideoService;

import java.io.File;
import java.util.ArrayList;

public class CreateVideoActivity extends BaseActivity {
    private String TAG = CreateVideoActivity.class.getName();
    private ActivityCreateVideoBinding binding;
    private MyApplication myApplication;
    private ArrayList<MyImageModel> listImage;
    private ArrayList<MyMusicModel> listMusic;
    private ArrayList<THEMES> listEffect;
    private ArrayList<MyTimerModel> listTimer;

    private ListImageFramgent listImageFramgent;
    private ListEffectsFramgent listEffectsFramgent;
    private ListMusicFramgent listMusicFramgent;
    private ListImageFramesFramgent listImageFramesFramgent;
    private TimerFramgent timerFramgent;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, CreateVideoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        setTitleToolbar("Khởi Tạo Video");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_video);
        myApplication = MyApplication.getInstance();
        listImage = myApplication.getListIamge();
        if (listImage == null) {
            ShowLog.ShowLog(this, binding.getRoot(), "Có lỗi, vui lòng thử lại sau", false);
            finish();
        }

        CreateVideoService.startService(this);

        getListMusic();

        initEffects();
        initTimer();

        initFragment();

        initViewPager();

        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager));
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        setupIconTablayout();
    }

    private void setupIconTablayout() {
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            binding.tabLayout.getTabAt(i).setIcon(R.drawable.ic_camera);
        }
    }

    private void initTimer() {
        if (listTimer == null) {
            listTimer = new ArrayList<>();
        }
        listTimer.clear();
        listTimer.add(new MyTimerModel(1, false));
        listTimer.add(new MyTimerModel(2, false));
        listTimer.add(new MyTimerModel(3, false));
        listTimer.add(new MyTimerModel(4, false));
        listTimer.add(new MyTimerModel(5, false));
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
        listImageFramgent = new ListImageFramgent(this, listImage);
        listEffectsFramgent = new ListEffectsFramgent(this, listEffect, myApplication);
        listMusicFramgent = new ListMusicFramgent(this, listMusic, myApplication);
        listImageFramesFramgent = new ListImageFramesFramgent();
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

        MyMusicModel tmp = new MyMusicModel();
        tmp.setSelected(true);
        tmp.setNameMusic("Demo");
        tmp.setPathMusic("android.resource://com.khacchung.makevideo/" + R.raw.a);
        listMusic.add(tmp);

        ContentResolver cr = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;

        if (cur != null) {
            count = cur.getCount();

            if (count > 0) {
                while (cur.moveToNext()) {
                    String pathMusic = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    File file = new File(pathMusic);
                    if (file.exists()) {
                        listMusic.add(new MyMusicModel(pathMusic, file.getName()));
                    }
                }
            }
        }
        cur.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //todo: listener event changed image
    }
}
