package com.khacchung.makevideo.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.fragment.ListEffectsFramgent;
import com.khacchung.makevideo.fragment.ListImageFramesFramgent;
import com.khacchung.makevideo.fragment.ListImageFramgent;
import com.khacchung.makevideo.fragment.ListMusicFramgent;
import com.khacchung.makevideo.fragment.QualityFramgent;
import com.khacchung.makevideo.fragment.TimerFramgent;
import com.khacchung.makevideo.util.CenteredImageSpan;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private Activity activity;

    private ListImageFramgent listImageFramgent;
    private ListEffectsFramgent listEffectsFramgent;
    private ListMusicFramgent listMusicFramgent;
    private ListImageFramesFramgent listImageFramesFramgent;
    private TimerFramgent timerFramgent;
    private QualityFramgent qualityFramgent;
    private Fragment currentFragment;

    private Drawable myDrawable[] = new Drawable[6];
    private String title[];

    public MyPagerAdapter(Activity activity,
                          FragmentManager fm,
                          ListImageFramgent listImageFramgent,
                          ListEffectsFramgent listEffectsFramgent,
                          ListMusicFramgent listMusicFramgent,
                          ListImageFramesFramgent listImageFramesFramgent,
                          TimerFramgent timerFramgent,
                          QualityFramgent qualityFramgent) {
        super(fm);
        this.activity = activity;
        this.listImageFramgent = listImageFramgent;
        this.listEffectsFramgent = listEffectsFramgent;
        this.listMusicFramgent = listMusicFramgent;
        this.listImageFramesFramgent = listImageFramesFramgent;
        this.timerFramgent = timerFramgent;
        this.qualityFramgent = qualityFramgent;

        currentFragment = listImageFramgent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myDrawable[0] = activity.getDrawable(R.drawable.ic_picture);
            myDrawable[1] = activity.getDrawable(R.drawable.ic_effect);
            myDrawable[2] = activity.getDrawable(R.drawable.ic_musical_note2);
            myDrawable[3] = activity.getDrawable(R.drawable.ic_frame);
            myDrawable[4] = activity.getDrawable(R.drawable.ic_clock_circular_outline);
            myDrawable[5] = activity.getDrawable(R.drawable.ic_resolution);
        }
        title = new String[]{
                activity.getResources().getString(R.string.list_image),
                activity.getResources().getString(R.string.list_effect),
                activity.getResources().getString(R.string.list_sound),
                activity.getResources().getString(R.string.list_frame),
                activity.getResources().getString(R.string.time),
                activity.getResources().getString(R.string.resolution)
        };
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
            case 5:
                currentFragment = qualityFramgent;
                break;
        }
        return currentFragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder sb = new SpannableStringBuilder(title[position]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myDrawable[position].setBounds(0, 0,
                    myDrawable[position].getIntrinsicWidth() - 10, myDrawable[position].getIntrinsicHeight() - 10);
            CenteredImageSpan span = new CenteredImageSpan(myDrawable[position], ImageSpan.ALIGN_BASELINE);
            sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return sb;
    }
}
