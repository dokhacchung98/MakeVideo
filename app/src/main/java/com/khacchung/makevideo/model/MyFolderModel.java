package com.khacchung.makevideo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.khacchung.makevideo.BR;

public class MyFolderModel extends BaseObservable {
    private String nameFolder;
    private String pathFolder;
    private boolean isSelected;
    private int sizeItem;

    public MyFolderModel() {
    }

    public MyFolderModel(String nameFolder, String pathFolder, boolean isSelected) {
        this.nameFolder = nameFolder;
        this.pathFolder = pathFolder;
        this.isSelected = isSelected;
    }

    public MyFolderModel(String nameFolder, String pathFolder, boolean isSelected, int sizeItem) {
        this.nameFolder = nameFolder;
        this.pathFolder = pathFolder;
        this.isSelected = isSelected;
        this.sizeItem = sizeItem;
    }

    @Bindable
    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
        notifyPropertyChanged(BR.nameFolder);
    }

    @Bindable
    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
        notifyPropertyChanged(BR.pathFolder);
    }

    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @Bindable
    public int getSizeItem() {
        return sizeItem;
    }

    public void setSizeItem(int sizeItem) {
        this.sizeItem = sizeItem;
        notifyPropertyChanged(BR.sizeItem);
    }
}
