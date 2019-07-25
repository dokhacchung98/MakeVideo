package com.khacchung.makevideo.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.fragment.CreatedImageFragment;
import com.khacchung.makevideo.fragment.CreatedVideoFragment;

public class MyPagerCreatedAdapter extends FragmentStatePagerAdapter {

    private Activity activity;

    private CreatedImageFragment createdImageFragment;
    private CreatedVideoFragment createdVideoFragment;

    private Fragment currentFragment;

    public MyPagerCreatedAdapter(@NonNull FragmentManager fm, Activity activity,
                                 CreatedImageFragment createdImageFragment,
                                 CreatedVideoFragment createdVideoFragment) {
        super(fm);
        this.activity = activity;
        this.createdImageFragment = createdImageFragment;
        this.createdVideoFragment = createdVideoFragment;

        currentFragment = createdImageFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                currentFragment = createdImageFragment;
                break;
            case 1:
                currentFragment = createdVideoFragment;
                break;
        }
        return currentFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return activity.getString(R.string.image_created);
            case 1:
                return activity.getString(R.string.video_created);
        }
        return "";
    }
}
