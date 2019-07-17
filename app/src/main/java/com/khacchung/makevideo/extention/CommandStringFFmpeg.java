package com.khacchung.makevideo.extention;

import android.content.Context;

import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.model.MyMusicModel;

import java.io.File;

public class CommandStringFFmpeg {
    public static String[] getCommandCreadVideoFromImageAndMusic(Context context, MyMusicModel myMusic, float timeLoad, String pathSaveVideo) {
        String pathFolderImage = MyPath.getPathTemp(context);
        File file = new File(pathFolderImage);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (myMusic != null) {
            File file1 = new File(myMusic.getPathMusic());
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

    public static String[] addVideoFrame(Context context, String pathVideoFrames, String pathSaveVideo) {
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
                        + MyApplication.VIDEO_WIDTH
                        + ":"
                        + MyApplication.VIDEO_HEIGHT
                        + ",geq=r='r(X,Y)':a='1*alpha(X,Y)'[s1];[0][s1]overlay=0:0",
                pathSaveVideo
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

    //ffmpeg -framerate 30 -i list_image_temp\img%04d.jpg -r 25 -pix_fmt yuv420p ouuu.mp4 --none sound
    //ffmpeg -framerate 15/5 -i list_image_temp\img%04d.jpg -i a.wav -r 25 -t total_time -c:v libx264 -preset ultrafast -pix_fmt yuv420p ouuu.mp4 --with sound
}
