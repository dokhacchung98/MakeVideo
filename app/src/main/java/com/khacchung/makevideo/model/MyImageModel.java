package com.khacchung.makevideo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khacchung.makevideo.BR;

import java.io.Serializable;

public class MyImageModel extends BaseObservable implements Serializable {
    private String pathImage;
    private boolean isSelected;
    private String pathParent;
    private int numberOfSeleted = 0;

    public MyImageModel() {
    }

    public MyImageModel(String pathImage, boolean isSelected) {
        this.pathImage = pathImage;
        this.isSelected = isSelected;
    }

    public MyImageModel(String pathImage, boolean isSelected, String pathParent) {
        this.pathImage = pathImage;
        this.isSelected = isSelected;
        this.pathParent = pathParent;
    }

    @Bindable
    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
        notifyPropertyChanged(BR.pathImage);
    }

    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public String getPathParent() {
        return pathParent;
    }

    public void setPathParent(String pathParent) {
        this.pathParent = pathParent;
    }

    @Bindable
    public int getNumberOfSeleted() {
        return numberOfSeleted;
    }

    public void setNumberOfSeleted(int numberOfSeleted) {
        this.numberOfSeleted = numberOfSeleted;
        notifyPropertyChanged(BR.numberOfSeleted);
    }

    @NonNull
    @Override
    public String toString() {
        return "MyImageModel()- pathImage: "
                + pathImage
                + " - isSelected: "
                + isSelected
                + " - path parent: "
                + pathParent;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return pathImage == ((MyImageModel) obj).getPathImage();
    }
}
