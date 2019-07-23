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
import com.khacchung.makevideo.activity.CutSoundActivity;
import com.khacchung.makevideo.adapter.ListMusicAdapter;
import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.base.BaseActivity;
import com.khacchung.makevideo.databinding.FragmentListMusicBinding;
import com.khacchung.makevideo.handler.MySelectedItemListener;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.util.CodeSelectedItem;

import java.util.ArrayList;

public class ListMusicFramgent extends Fragment implements MySelectedItemListener {
    private FragmentListMusicBinding binding;
    private BaseActivity baseActivity;
    private ArrayList<MyMusicModel> listMusic;
    private ListMusicAdapter listMusicAdapter;
    private MyApplication myApplication;
    private boolean isAlertOpen = false;

    public ListMusicFramgent(BaseActivity baseActivity, ArrayList<MyMusicModel> listMusic, MyApplication myApplication) {
        this.baseActivity = baseActivity;
        this.listMusic = listMusic;
        this.myApplication = myApplication;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_list_music,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        listMusicAdapter = new ListMusicAdapter(baseActivity, listMusic, this);
        binding.setMyAdapter(listMusicAdapter);
        binding.setLayoutManager(new LinearLayoutManager(baseActivity));
        openOrCloseAlert();
    }

    @Override
    public void selectedItem(Object obj, int code) {
        if (code == CodeSelectedItem.CODE_SELECT) {
            MyMusicModel myMusicModel = (MyMusicModel) obj;
            myApplication.setMyMusicModel(myMusicModel);
            for (MyMusicModel tmp : listMusic) {
                tmp.setSelected(false);
            }
            int index = listMusic.indexOf(myMusicModel);
            if (index != -1) {
                listMusic.get(index).setSelected(true);
            }
            listMusicAdapter.notifyDataSetChanged();
            //todo: broadcast event changed the music

            baseActivity.onChangedMusic();
        } else {
            MyMusicModel myMusicModel = (MyMusicModel) obj;
            CutSoundActivity.startIntent(baseActivity, myMusicModel.getPathMusic());
        }
    }

    public void openOrCloseAlert() {
        if (binding != null) {
            isAlertOpen = myApplication.isAlertSound();
            if (isAlertOpen) {
                binding.txtAlert.setVisibility(View.VISIBLE);
            } else {
                binding.txtAlert.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void selectedItem(Object obj, int code, int p) {

    }
}
