package com.khacchung.makevideo.activity;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.MyPagerCreatedAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.ActivityCreatedFileBinding;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.fragment.CreatedImageFragment;
import com.khacchung.makevideo.fragment.CreatedVideoFragment;
import com.khacchung.makevideo.model.MyVideoModel;

import java.io.File;
import java.util.ArrayList;

public class CreatedFileActivity extends BaseActivity {
    private static final String TAG = CreatedFileActivity.class.getName();
    private static final String PATH_VIDEO = "path_video";
    private ActivityCreatedFileBinding binding;
    private CreatedImageFragment createdImageFragment;
    private CreatedVideoFragment createdVideoFragment;
    private MyPagerCreatedAdapter myPagerCreatedAdapter;
    private ArrayList<String> listImages = new ArrayList<>();
    private ArrayList<MyVideoModel> listVideos = new ArrayList<>();
    private String pathVideo = null;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, CreatedFileActivity.class);
        activity.startActivity(intent);
    }

    public static void startIntentWithVideo(Activity activity, String pathVideo) {
        Intent intent = new Intent(activity, CreatedFileActivity.class);
        intent.putExtra(PATH_VIDEO, pathVideo);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_created_file);

        Intent intent = getIntent();
        String pathVideo = intent.getStringExtra(PATH_VIDEO);

        initData();
        initUI();

        if (pathVideo != null && !pathVideo.isEmpty()) {
            PlayVideoActivity.startIntent(CreatedFileActivity.this, pathVideo);
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (pathVideo != null && !pathVideo.isEmpty()) {
            binding.vpCreated.setCurrentItem(1);
            PlayVideoActivity.startIntent(this, pathVideo);
        }
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        setTitleToolbar(getString(R.string.file_created));
        enableBackButton();

        binding.tlCreated.setupWithViewPager(binding.vpCreated);
        binding.tlCreated.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.vpCreated));
        binding.vpCreated.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tlCreated));

        createdImageFragment = new CreatedImageFragment(this, listImages);
        createdVideoFragment = new CreatedVideoFragment(this, listVideos);

        FragmentManager manager = getSupportFragmentManager();
        myPagerCreatedAdapter = new MyPagerCreatedAdapter(manager, this, createdImageFragment, createdVideoFragment);
        binding.setAdapter(myPagerCreatedAdapter);
    }

    private void initData() {
        getAllImagesIsCreated();
        getAllVideosIsCreated();
    }

    private void getAllVideosIsCreated() {
        listVideos.clear();
        String parentPath = MyPath.getPathSaveVideo(this);
        File file3 = new File(parentPath);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        String thumbnailPath = MyPath.getPathThumbnail(this);
        File file2 = new File(thumbnailPath);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file[] = new File(parentPath).listFiles();
        if (file != null)
            if (file.length > 0)
                for (File f : file) {
                    String name = f.getName();
                    listVideos.add(new MyVideoModel(f.getAbsolutePath(),
                            MyPath.getPathThumbnail(this)
                                    + name.replaceAll(".mp4", ".png")));
                    Log.e(TAG, "getAllVideosIsCreated(): " + f.getAbsolutePath());
                }
    }

    private void getAllImagesIsCreated() {
        listImages.clear();
        String parentPath = MyPath.getPathSaveImage(this);
        File file2 = new File(parentPath);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File listFile[] = new File(parentPath).listFiles();
        if (listFile != null)
            if (listFile.length > 0)
                if (listFile != null) {
                    for (File file : listFile) {
                        if (!file.isDirectory()) {
                            if (file.getName().endsWith(".png")
                                    || file.getName().endsWith(".jpg")
                                    || file.getName().endsWith(".jpeg")) {
                                if (!listImages.contains(file.getAbsolutePath()))
                                    listImages.add(file.getAbsolutePath());
                                Log.e(TAG, "getAllImagesIsCreated(): " + file.getAbsolutePath());
                            }
                        }
                    }
                }
    }
}
