package com.khacchung.makevideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.khacchung.makevideo.R;
import com.khacchung.makevideo.databinding.FragmentCreatedVideoBinding;

public class CreatedVideoFragment extends Fragment {
    private FragmentCreatedVideoBinding binding;

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
}
