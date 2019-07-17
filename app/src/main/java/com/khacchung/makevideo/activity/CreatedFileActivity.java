package com.khacchung.makevideo.activity;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.MyPagerCreatedAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.ActivityCreatedFileBinding;
import com.khacchung.makevideo.extention.GetFileFromURI;
import com.khacchung.makevideo.extention.MyPath;
import com.khacchung.makevideo.fragment.CreatedImageFragment;
import com.khacchung.makevideo.fragment.CreatedVideoFragment;
import com.khacchung.makevideo.model.MyVideoModel;

import java.io.File;
import java.util.ArrayList;

public class CreatedFileActivity extends BaseActivity {
    private ActivityCreatedFileBinding binding;

    private CreatedImageFragment createdImageFragment;
    private CreatedVideoFragment createdVideoFragment;

    private MyPagerCreatedAdapter myPagerCreatedAdapter;

    private ArrayList<String> listImages = new ArrayList<>();
    private ArrayList<MyVideoModel> listVideos = new ArrayList<>();

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, CreatedFileActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_created_file);
        initData();
        initUI();
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);
        setTitleToolbar("Danh Sách Đã Tạo");
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
        File file[] = new File(parentPath).listFiles();
        for (File f : file) {
            String name = f.getName().substring(0, f.getName().indexOf("."));
            listVideos.add(new MyVideoModel(f.getAbsolutePath(),
                    MyPath.getPathThumbnail(this) + name + ".png"));
        }
    }

    private void getAllImagesIsCreated() {
        listImages.clear();
        String parentPath = MyPath.getPathSaveImage(this);
        File listFile[] = new File(parentPath).listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (!file.isDirectory()) {
                    if (file.getName().endsWith(".png")
                            || file.getName().endsWith(".jpg")
                            || file.getName().endsWith(".jpeg")) {
                        String temp = file.getPath().substring(0, file.getPath().lastIndexOf('/'));
                        if (!listImages.contains(temp))
                            listImages.add(temp);
                    }
                }
            }
        }
    }
}
