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
import com.khacchung.makevideo.activity.ShowImageFullScreenActivity;
import com.khacchung.makevideo.adapter.ListImageIsCreatedAdapter;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentCreatedImageBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.io.File;
import java.util.ArrayList;

public class CreatedImageFragment extends Fragment implements MySelectedItemListener {
    private FragmentCreatedImageBinding binding;
    private ArrayList<String> listImage;
    private ListImageIsCreatedAdapter listImageIsCreatedAdapter;
    private BaseActivity baseActivity;

    public CreatedImageFragment(BaseActivity baseActivity, ArrayList<String> listImage) {
        this.listImage = listImage;
        this.baseActivity = baseActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_created_image,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listImageIsCreatedAdapter = new ListImageIsCreatedAdapter(baseActivity, listImage, this);
        binding.setAdapter(listImageIsCreatedAdapter);
        binding.setLayoutManager(new GridLayoutManager(baseActivity, 2));
    }

    @Override
    public void selectedItem(Object obj, int code) {

    }

    @Override
    public void selectedItem(Object obj, int code, int p) {
        if (code == CodeSelectedItem.CODE_SHOW_IMAGE && p < listImage.size()) {
            MyImageModel model = new MyImageModel();
            String path = (String) obj;
            model.setPathImage(path);
            File file = new File(path);
            if (file.exists()) {
                model.setPathParent(file.getParent());
            }
            model.setSelected(false);
            model.setNumberOfSeleted(0);
            ShowImageFullScreenActivity.startIntent(baseActivity, model);
        } else if (code == CodeSelectedItem.CODE_REMOVE && p < listImage.size()) {
            baseActivity.showDialogDeleteFile(obj.toString(), binding.getRoot(), () -> {
                listImage.remove(p);
                listImageIsCreatedAdapter.notifyDataSetChanged();
            });
        }
    }
}
