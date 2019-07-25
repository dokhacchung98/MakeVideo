package com.khacchung.makevideo.extention;

import android.content.Context;

import com.khacchung.makevideo.application.MyApplication;

import java.io.File;

public class CommandStringFFmpeg {
    public static String[] getCommandCreadVideoFromImageAndMusic(Context context, String myMusic, float timeLoad, String pathSaveVideo) {
        String pathFolderImage = MyPath.getPathTemp(context);
        File file = new File(pathFolderImage);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!myMusic.isEmpty()) {
            File file1 = new File(myMusic);
            String tmp[] = new String[]{
                    "-y",
                    "-framerate",
                    "15/" + timeLoad,
                    "-i",
                    pathFolderImage + "img%04d.jpg",
                    "-i",
                    file1.getAbsolutePath(),
                    "-r",
                    "25",
                    "-shortest",
                    "-c:v",
                    "libx264",
                    "-preset",
                    "ultrafast",
                    "-pix_fmt",
                    "yuv420p",
                    pathSaveVideo
            };
            return tmp;
        } else {
            String tmp[] = new String[]{
                    "-y",
                    "-framerate",
                    "15/" + timeLoad,
                    "-i",
                    pathFolderImage + "img%04d.jpg",
                    "-r",
                    "25",
                    "-pix_fmt",
                    "yuv420p",
                    pathSaveVideo
            };
            return tmp;
        }
    }

    public static String[] addVideoFrame(Context context, String pathVideoFrames, String pathSaveVideo, MyApplication myApplication) {
        String pathVideo = MyPath.getPathTempVideo(context) + MyPath.NAME_VIDEO_TEMP;
        File file = new File(pathVideo);
        if (!file.exists()) {
            return null;
        }
        return new String[]{
                "-y",
                "-i",
                pathVideo,
                "-i",
                pathVideoFrames,
                "-filter_complex",
                "[1]scale="
                        + myApplication.getQuality().getWidth()
                        + ":"
                        + myApplication.getQuality().getHeight()
                        + ",geq=r='r(X,Y)':a='1*alpha(X,Y)'[s1];[0][s1]overlay=0:0",
                pathSaveVideo
        };
    }

    public static String[] moveVideo(Context context, String pathSave) {
        String pathVideo = MyPath.getPathTempVideo(context) + MyPath.NAME_VIDEO_TEMP;
        File file = new File(pathVideo);
        if (!file.exists()) {
            return null;
        }
        return new String[]{
                "-y",
                "-i",
                file.getAbsolutePath(),
                pathSave
        };
    }

    public static String[] copyThumbnail(Context context, String sourceImage, String nameImage) {
        String parentPath = MyPath.getPathThumbnail(context);
        File file = new File(parentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return new String[]{
                "-y",
                "-i",
                sourceImage,
                parentPath + nameImage + ".png"
        };
    }

    public static String[] cutSound(Context context, String pathSound, float startSecond, float sizeSound) {
        String pathParent = MyPath.getPathTempSound(context);
        File file = new File(pathParent);
        if (!file.exists()) {
            file.mkdirs();
        }

        File fileSound = new File(pathParent + MyPath.NAME_SOUND_TEMP);

        return new String[]{
                "-y",
                "-i",
                pathSound,
                "-ss",
                startSecond + "",
                "-t",
                sizeSound + "",
                "-acodec",
                "copy",
                fileSound.getAbsolutePath()
        };
    }
}
