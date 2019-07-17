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
import com.khacchung.makevideo.adapter.ListFramesAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentListVideoFrameBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyFrameModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListImageFramesFramgent extends Fragment implements MySelectedItemListener {
    private FragmentListVideoFrameBinding binding;
    private BaseActivity baseActivity;
    private MyApplication myApplication;
    private ArrayList<MyFrameModel> listFrame;
    private ListFramesAdapter listFramesAdapter;

    public ListImageFramesFramgent(BaseActivity baseActivity,
                                   ArrayList<MyFrameModel> listFrame,
                                   MyApplication myApplication) {
        this.baseActivity = baseActivity;
        this.myApplication = myApplication;
        this.listFrame = listFrame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_list_video_frame,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listFramesAdapter = new ListFramesAdapter(baseActivity, listFrame, this);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.setMyAdapter(listFramesAdapter);
    }

    @Override
    public void selectedItem(Object obj, int code) {
        if (code == CodeSelectedItem.CODE_SELECT) {
            MyFrameModel frame = (MyFrameModel) obj;
            myApplication.setFrameVideo(frame.getPathFrame());
            listFramesAdapter.getCurrentTheme();
            listFramesAdapter.notifyDataSetChanged();

            //todo: broadcast event changed the frame
            baseActivity.onChangedVideoFrame();
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {

    }
}
