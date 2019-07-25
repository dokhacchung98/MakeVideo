package com.khacchung.makevideo.model;

public class MyVector {
    private int width;
    private int height;

    public MyVector() {
    }

    public MyVector(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
