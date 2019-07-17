package com.khacchung.makevideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.adapter.ListVideosCreatedAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentCreatedVideoBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyVideoModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class CreatedVideoFragment extends Fragment implements MySelectedItemListener {
    private FragmentCreatedVideoBinding binding;
    private ArrayList<MyVideoModel> listVideos;
    private ListVideosCreatedAdapter listVideosCreatedAdapter;
    private BaseActivity baseActivity;

    public CreatedVideoFragment(BaseActivity baseActivity, ArrayList<MyVideoModel> listVideos) {
        this.listVideos = listVideos;
        this.baseActivity = baseActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_created_video,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listVideosCreatedAdapter = new ListVideosCreatedAdapter(baseActivity, listVideos, this);
        binding.setAdapter(listVideosCreatedAdapter);
        binding.setLayoutManager(new GridLayoutManager(baseActivity, 2));
    }

    @Override
    public void selectedItem(Object obj, int code) {

    }

    @Override
    public void selectedItem(Object obj, int code, int p) {
        if (code == CodeSelectedItem.CODE_SHOW_VIDEO && p < listVideos.size()) {
            //todo: show video
        }
    }
}
