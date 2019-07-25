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
import com.khacchung.makevideo.adapter.ListQualityAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentQualityBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyQualityModel;
import com.khacchung.makevideo.model.MyTimerModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class QualityFramgent extends Fragment implements MySelectedItemListener {
    private FragmentQualityBinding binding;
    private BaseActivity baseActivity;
    private MyApplication myApplication;
    private ArrayList<MyQualityModel> listQuality;
    private ListQualityAdapter listTimerAdapter;

    public QualityFramgent(BaseActivity baseActivity, MyApplication myApplication, ArrayList<MyQualityModel> listQuality) {
        this.baseActivity = baseActivity;
        this.myApplication = myApplication;
        this.listQuality = listQuality;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_quality,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listTimerAdapter = new ListQualityAdapter(baseActivity, listQuality, this);
        binding.setMyAdapter(listTimerAdapter);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity));
    }

    @Override
    public void selectedItem(Object obj, int code) {
        if (code == CodeSelectedItem.CODE_SELECT) {
            MyQualityModel model = (MyQualityModel) obj;
            myApplication.setQuality(model.getQuality());
            for (MyQualityModel tmp : listQuality) {
                tmp.setSelected(false);
            }
            int index = listQuality.indexOf(model);
            if (index != -1) {
                listQuality.get(index).setSelected(true);
            }
            listTimerAdapter.notifyDataSetChanged();

            baseActivity.onChangedQuality();
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {

    }
}
