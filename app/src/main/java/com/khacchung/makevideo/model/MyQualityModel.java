package com.khacchung.makevideo.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class MyQualityModel extends BaseObservable {
    private EnumQuality quality;
    private boolean isSelected;

    public MyQualityModel() {

    }

    public MyQualityModel(String quality, boolean isSelected) {
        this.quality = EnumQuality.getByName(quality);
        this.isSelected = isSelected;
    }

    @Bindable
    public EnumQuality getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = EnumQuality.getByName(quality);
        notifyPropertyChanged(BR.quality);
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
        return "MyQualityModel() - quality: "
                + quality.getName()
                + " - isSelected: "
                + isSelected;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((MyQualityModel) obj).quality.getName() == quality.getName();
    }
}
