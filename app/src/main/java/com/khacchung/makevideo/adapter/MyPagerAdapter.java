package com.khacchung.makevideo.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.khacchung.makevideo.fragment.ListEffectsFramgent;
import com.khacchung.makevideo.fragment.ListImageFramesFramgent;
import com.khacchung.makevideo.fragment.ListImageFramgent;
import com.khacchung.makevideo.fragment.ListMusicFramgent;
import com.khacchung.makevideo.fragment.TimerFramgent;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private Activity activity;

    private ListImageFramgent listImageFramgent;
    private ListEffectsFramgent listEffectsFramgent;
    private ListMusicFramgent listMusicFramgent;
    private ListImageFramesFramgent listImageFramesFramgent;
    private TimerFramgent timerFramgent;
    private Fragment currentFragment;

    public MyPagerAdapter(Activity activity,
                          FragmentManager fm,
                          ListImageFramgent listImageFramgent,
                          ListEffectsFramgent listEffectsFramgent,
                          ListMusicFramgent listMusicFramgent,
                          ListImageFramesFramgent listImageFramesFramgent,
                          TimerFramgent timerFramgent) {
        super(fm);
        this.activity = activity;
        this.listImageFramgent = listImageFramgent;
        this.listEffectsFramgent = listEffectsFramgent;
        this.listMusicFramgent = listMusicFramgent;
        this.listImageFramesFramgent = listImageFramesFramgent;
        this.timerFramgent = timerFramgent;

        currentFragment = listImageFramgent;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                currentFragment = listImageFramgent;
                break;
            case 1:
                currentFragment = listEffectsFramgent;
                break;
            case 2:
                currentFragment = listMusicFramgent;
                break;
            case 3:
                currentFragment = listImageFramesFramgent;
                break;
            case 4:
                currentFragment = timerFramgent;
                break;
        }
        return currentFragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Danh Sách Ảnh";
            case 1:
                return "Danh Sách Hiệu Ứng";
            case 2:
                return "Danh Sách Nhạc";
            case 3:
                return "Danh Sách Khung";
            case 4:
                return "Thời Gian";
        }
        return "";
    }
}
