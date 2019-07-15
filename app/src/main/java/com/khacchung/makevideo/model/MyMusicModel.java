package com.khacchung.makevideo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class MyMusicModel extends BaseObservable {
    private String pathMusic;
    private String nameMusic;
    private boolean isSelected;

    public MyMusicModel() {
    }

    public MyMusicModel(String pathMusic, String nameMusic) {
        this.pathMusic = pathMusic;
        this.nameMusic = nameMusic;
        isSelected = false;
    }

    @Bindable
    public String getPathMusic() {
        return pathMusic;
    }

    public void setPathMusic(String pathMusic) {
        this.pathMusic = pathMusic;
        notifyPropertyChanged(BR.pathMusic);
    }

    @Bindable
    public String getNameMusic() {
        return nameMusic;
    }

    public void setNameMusic(String nameMusic) {
        this.nameMusic = nameMusic;
        notifyPropertyChanged(BR.nameMusic);
    }

    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @NonNull
    @Override
    public String toString() {
        return "MyMusicModel() - name: "
                + nameMusic
                + " -path: "
                + pathMusic;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        assert obj != null;
        return ((MyMusicModel) obj).pathMusic.equals(pathMusic);
    }
}
