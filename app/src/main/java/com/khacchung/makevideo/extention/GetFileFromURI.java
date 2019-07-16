package com.khacchung.makevideo.extention;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;

import java.io.File;
import java.util.ArrayList;

public class GetFileFromURI {
    public static ArrayList<MyMusicModel> getAllMusicFromURI(Context context, String uri) {
        ArrayList<MyMusicModel> listMusic = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(Uri.parse(uri), null, selection, null, sortOrder);
        int count = 0;

        if (cur != null) {
            count = cur.getCount();

            if (count > 0) {
                while (cur.moveToNext()) {
                    String pathMusic = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    File file = new File(pathMusic);
                    if (file.exists()) {
                        listMusic.add(new MyMusicModel(pathMusic, file.getName()));
                    }
                }
            }
        }
        cur.close();
        return listMusic;
    }

    public static ArrayList<String> getAllVideoFromURI(Context context, String uri) {
        ArrayList<String> listvideo = new ArrayList<>();
        Cursor cursor;
        int column_index_data;

        String absolutePathOfImage;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getApplicationContext().getContentResolver().query(Uri.parse(uri), projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listvideo.add(absolutePathOfImage);
        }
        return listvideo;
    }

    public static ArrayList<String> getAllImageFromURI(Context context, String uri) {
        ArrayList<String> listImage = new ArrayList<>();
        File listFile[] = new File(uri).listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (!file.isDirectory()) {
                    if (file.getName().endsWith(".png")
                            || file.getName().endsWith(".jpg")
                            || file.getName().endsWith(".jpeg")) {
                        String temp = file.getPath().substring(0, file.getPath().lastIndexOf('/'));
                        if (!listImage.contains(temp))
                            listImage.add(temp);
                    }
                }
            }
        }
        return listImage;
    }
}
