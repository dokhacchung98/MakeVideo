package com.khacchung.makevideo.extention;

import android.content.Context;

public class MyPath {
    public static final String NAME_VIDEO_TEMP = "temp_video.mp4";
    public static final String NAME_SOUND_TEMP = "temp_sound.mp3";

    public static String getPathTemp(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_image_temp/";
    }

    public static String getPathSaveImage(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_image_edit/";
    }

    public static String getPathSaveVideo(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_video_edit/";
    }

    public static String getPathTempVideo(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/video_temp/";
    }

    public static String getPathFrame(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/frames/";
    }

    public static String getPathSound(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/sound/";
    }

    public static String getPathThumbnail(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_thumbnail/";
    }

    public static String getPathTempSound(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_temp_sound/";
    }
}
