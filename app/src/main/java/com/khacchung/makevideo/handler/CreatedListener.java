package com.khacchung.makevideo.handler;

public interface CreatedListener {
    void onSuccess(int numberOfFrames);

    void onUpdate(int size);

    void onStartCreateVideo();
}
