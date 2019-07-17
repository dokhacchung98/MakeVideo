package com.khacchung.makevideo.extention;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.khacchung.makevideo.model.MyImageModel;
import com.khacchung.makevideo.model.MyMusicModel;
import com.khacchung.makevideo.model.MyVideoModel;

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
}
