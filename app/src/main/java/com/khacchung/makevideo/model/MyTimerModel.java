package com.khacchung.makevideo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class MyTimerModel extends BaseObservable {
    private int time;
    private boolean isSelected;

    public MyTimerModel() {
    }

    public MyTimerModel(int time, boolean isSelected) {
        this.time = time;
        this.isSelected = isSelected;
    }

    @Bindable
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
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
        return "MyTimerModel() - time: "
                + time
                + " - isSelected: "
                + isSelected;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((MyTimerModel) obj).time == time;
    }
}
