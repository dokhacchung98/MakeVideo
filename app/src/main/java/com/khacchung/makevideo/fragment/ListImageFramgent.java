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
import com.khacchung.makevideo.adapter.ListImageHorizontalAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentListImageBinding;
import com.khacchung.makevideo.handler.MyClickHandler;
import com.khacchung.makevideo.model.MyImageModel;

import java.util.ArrayList;

public class ListImageFramgent extends Fragment implements MyClickHandler {
    private FragmentListImageBinding binding;
    private BaseActivity baseActivity;
    private ListImageHorizontalAdapter listImageHorizontalAdapter;
    private ArrayList<MyImageModel> listImage;

    public ListImageFramgent(BaseActivity baseActivity, ArrayList<MyImageModel> listImage) {
        this.baseActivity = baseActivity;
        this.listImage = listImage;
//        MyImageModel tmp = new MyImageModel();
//        tmp.setSelected(false);
//        tmp.setPathImage("");
//        this.listImage.add(tmp);
    }

    public void updateListImage(ArrayList<MyImageModel> listImage) {
        this.listImage = listImage;
        if (listImageHorizontalAdapter != null) {
            listImageHorizontalAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_list_image,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listImageHorizontalAdapter = new ListImageHorizontalAdapter(baseActivity, listImage);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.setMyAdapter(listImageHorizontalAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClickWithData(View view, Object value) {

    }
}
