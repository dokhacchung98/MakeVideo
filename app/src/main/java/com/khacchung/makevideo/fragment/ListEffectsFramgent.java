package com.khacchung.makevideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.ListEffectsHorizontalAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentListEffectBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.util.CodeSelectedItem;
import com.khacchung.makevideo.mask.THEMES;

import java.util.ArrayList;

public class ListEffectsFramgent extends Fragment implements MySelectedItemListener {
    private FragmentListEffectBinding binding;
    private BaseActivity baseActivity;
    private MyApplication myApplication;
    private ArrayList<THEMES> listEffects;
    private ListEffectsHorizontalAdapter listEffectsHorizontalAdapter;

    public ListEffectsFramgent(BaseActivity baseActivity,
                               ArrayList<THEMES> listEffects,
                               MyApplication myApplication) {
        this.baseActivity = baseActivity;
        this.listEffects = listEffects;
        this.myApplication = myApplication;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_list_effect,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listEffectsHorizontalAdapter = new ListEffectsHorizontalAdapter(baseActivity, listEffects, this);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.setMyAdapter(listEffectsHorizontalAdapter);
    }

    @Override
    public void selectedItem(Object obj, int code) {
        if (code == CodeSelectedItem.CODE_SELECT) {
            THEMES effect = (THEMES) obj;
            myApplication.setSeletedTheme(effect);
            listEffectsHorizontalAdapter.getCurrentTheme();
            listEffectsHorizontalAdapter.notifyDataSetChanged();

            //todo: broadcast event changed the effect
            baseActivity.onChangedEffect();
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {

    }
}
