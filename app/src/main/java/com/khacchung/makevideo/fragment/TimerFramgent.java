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
import com.khacchung.makevideo.adapter.ListTimerAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentTimeBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyTimerModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class TimerFramgent extends Fragment implements MySelectedItemListener {
    private FragmentTimeBinding binding;
    private BaseActivity baseActivity;
    private MyApplication myApplication;
    private ArrayList<MyTimerModel> listTimer;
    private ListTimerAdapter listTimerAdapter;

    public TimerFramgent(BaseActivity baseActivity, MyApplication myApplication, ArrayList<MyTimerModel> listTimer) {
        this.baseActivity = baseActivity;
        this.myApplication = myApplication;
        this.listTimer = listTimer;
        listTimer.get(0).setSelected(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_time,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listTimerAdapter = new ListTimerAdapter(baseActivity, listTimer, this);
        binding.setMyAdapter(listTimerAdapter);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity));
    }

    @Override
    public void selectedItem(Object obj, int code) {
        if (code == CodeSelectedItem.CODE_SELECT) {
            MyTimerModel model = (MyTimerModel) obj;
            myApplication.setTimeLoad(model.getTime());
            for (MyTimerModel tmp : listTimer) {
                tmp.setSelected(false);
            }
            int index = listTimer.indexOf(model);
            if (index != -1) {
                listTimer.get(index).setSelected(true);
            }
            listTimerAdapter.notifyDataSetChanged();

            baseActivity.onChangedTimeFrame();
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {

    }
}
