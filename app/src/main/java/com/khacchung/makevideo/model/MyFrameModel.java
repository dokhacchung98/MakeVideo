package com.khacchung.makevideo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khacchung.makevideo.BR;

public class MyFrameModel extends BaseObservable {
    private String pathFrame;
    private boolean isSelected;

    public MyFrameModel() {
    }

    public MyFrameModel(String pathFrame) {
        this.pathFrame = pathFrame;
    }

    @Bindable
    public String getPathFrame() {
        return pathFrame;
    }

    public void setPathFrame(String pathFrame) {
        this.pathFrame = pathFrame;
        notifyPropertyChanged(BR.pathFrame);
    }

    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((MyFrameModel) obj).getPathFrame().equals(this.pathFrame);
    }

    @NonNull
    @Override
    public String toString() {
        return "MyFrameModel() path: "
                + pathFrame
                + " - isSelected: "
                + isSelected;
    }
}
