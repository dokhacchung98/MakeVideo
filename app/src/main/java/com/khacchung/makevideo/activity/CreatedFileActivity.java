package com.khacchung.makevideo.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.MyPagerCreatedAdapter;
import com.khacchung.makevideo.databinding.ActivityCreatedFileBinding;
import com.khacchung.makevideo.fragment.CreatedImageFragment;
import com.khacchung.makevideo.fragment.CreatedVideoFragment;

public class CreatedFileActivity extends AppCompatActivity {
    private ActivityCreatedFileBinding binding;

    private CreatedImageFragment createdImageFragment;
    private CreatedVideoFragment createdVideoFragment;

    private MyPagerCreatedAdapter myPagerCreatedAdapter;

    public static void startIntent(Activity activity) {
        Intent intent = new Intent(activity, CreatedFileActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_created_file);

        binding.tlCreated.setupWithViewPager(binding.vpCreated);
        binding.tlCreated.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.vpCreated));
        binding.vpCreated.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tlCreated));

        createdImageFragment = new CreatedImageFragment();
        createdVideoFragment = new CreatedVideoFragment();

        FragmentManager manager = getSupportFragmentManager();
        myPagerCreatedAdapter = new MyPagerCreatedAdapter(manager, this, createdImageFragment, createdVideoFragment);
        binding.setAdapter(myPagerCreatedAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }
}
