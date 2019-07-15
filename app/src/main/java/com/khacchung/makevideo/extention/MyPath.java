package com.khacchung.makevideo.extention;

import android.content.Context;

public class MyPath {
    public static String getPathTemp(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_image_temp";
    }

    public static String getPathSaveImage(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_image_edit";
    }

    public static String getPathSaveVideo(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/list_video_edit";
    }
}
