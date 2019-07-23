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
import com.khacchung.makevideo.activity.EditImageActivity;
import com.khacchung.makevideo.activity.SelectImageActivity;
import com.khacchung.makevideo.adapter.ListImageHorizontalAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentListImageBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListImageFramgent extends Fragment implements MySelectedItemListener {
    private FragmentListImageBinding binding;
    private BaseActivity baseActivity;
    private ListImageHorizontalAdapter listImageHorizontalAdapter;
    private ArrayList<MyImageModel> listImage;
    private MyApplication myApplication;

    public ListImageFramgent(BaseActivity baseActivity, ArrayList<MyImageModel> listImage, MyApplication myApplication) {
        this.baseActivity = baseActivity;
        this.listImage = listImage;
        this.myApplication = myApplication;
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
        listImageHorizontalAdapter = new ListImageHorizontalAdapter(baseActivity, listImage, this);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity, LinearLayoutManager.HORIZONTAL, false));
        binding.setMyAdapter(listImageHorizontalAdapter);
    }

    @Override
    public void selectedItem(Object obj, int code) {

    }

    @Override
    public void selectedItem(Object obj, int code, int p) {
        if (code == CodeSelectedItem.CODE_SELECT) {
            MyImageModel model = (MyImageModel) obj;
            myApplication.setEnd(false);
            EditImageActivity.startInterntWithIndex(baseActivity, model.getPathImage(), p, binding.getRoot(), true);
        } else if (code == CodeSelectedItem.CODE_ADD) {
            SelectImageActivity.startIntent(baseActivity);
            baseActivity.finish();
        }
    }
}
