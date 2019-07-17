package com.khacchung.makevideo.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.IBinder;

import com.khacchung.makevideo.application.MyApplication;
import com.khacchung.makevideo.mask.FinalMaskBitmap;
import com.khacchung.makevideo.mask.ScalingUtilities;
import com.khacchung.makevideo.model.MyImageModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateVideoService extends Service {
    private int indexTempRun;
    private MyApplication myApplication;
    private ArrayList<MyImageModel> listImage;

    private Handler handler;

    public static void startService(Activity activity) {
        Intent intent = new Intent(activity, CreateVideoService.class);
        activity.startService(intent);
    }

    public static void stopService(Activity activity) {
        MyApplication application = MyApplication.getInstance();
        if (application.checkServiceCreateVideoIsRunning()) {
            Intent intent = new Intent(activity, CreateVideoService.class);
            activity.stopService(intent);
        }
    }

    public CreateVideoService() {
        myApplication = MyApplication.getInstance();
        listImage = myApplication.getListIamge();

        handler = new Handler();
        handler.post(() -> createImages());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createImages() {
        myApplication.removeAllImage();
        indexTempRun = 0;
        Bitmap newFirstBmp;
        Bitmap newSecondBmp2 = null;
        int i = 0;
        String pathImage = myApplication.getPathSaveTempImage();
        File imgDir = new File(pathImage);
        if (imgDir.exists()) {
            imgDir.delete();
        }
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        while (i < listImage.size() - 1) {
            if (i == 0) {
                Bitmap firstBitmap = ScalingUtilities.checkBitmap(listImage.get(i).getPathImage());
                Bitmap temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                newFirstBmp = ScalingUtilities.ConvetrSameSize(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
                temp.recycle();
                firstBitmap.recycle();
                System.gc();
            } else {
                if (newSecondBmp2 == null || newSecondBmp2.isRecycled()) {
                    Bitmap firstBitmap2 = ScalingUtilities.checkBitmap((listImage.get(i)).getPathImage());
                    Bitmap temp2 = ScalingUtilities.scaleCenterCrop(firstBitmap2, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    newSecondBmp2 = ScalingUtilities.ConvetrSameSize(firstBitmap2, temp2, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
                    temp2.recycle();
                    firstBitmap2.recycle();
                }
                newFirstBmp = newSecondBmp2;
            }
            Bitmap secondBitmap = ScalingUtilities.checkBitmap((listImage.get(i + 1)).getPathImage());
            Bitmap temp22 = ScalingUtilities.scaleCenterCrop(secondBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            newSecondBmp2 = ScalingUtilities.ConvetrSameSize(secondBitmap, temp22, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
            temp22.recycle();
            secondBitmap.recycle();
            System.gc();
            FinalMaskBitmap.reintRect();
            FinalMaskBitmap.EFFECT effect = myApplication.getSeletedTheme().getTheme().get(i % myApplication.getSeletedTheme().getTheme().size());
            for (int j = 0; ((float) j) < FinalMaskBitmap.ANIMATED_FRAME; j++) {
                Bitmap bmp = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Paint paint = new Paint(1);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                Canvas canvas = new Canvas(bmp);
                canvas.drawBitmap(newFirstBmp, 0.0f, 0.0f, null);
                canvas.drawBitmap(effect.getMask(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, j), 0.0f, 0.0f, paint);
                Bitmap bitmap3 = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmap3);
                canvas2.drawBitmap(newSecondBmp2, 0.0f, 0.0f, null);
                canvas2.drawBitmap(bmp, 0.0f, 0.0f, new Paint());

                File file = imgDir;
                File file2 = new File(file, String.format("img" + "%04d.jpg", new Object[]{Integer.valueOf(indexTempRun)}));
                try {
                    if (file2.exists()) {
                        file2.delete();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                indexTempRun++;
            }
            i++;
        }

        if (myApplication.getListener() != null) {
            myApplication.getListener().onSuccess(indexTempRun);
        }
        this.stopSelf();
    }
}
