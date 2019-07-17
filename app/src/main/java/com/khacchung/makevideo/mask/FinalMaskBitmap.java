package com.khacchung.makevideo.mask;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.core.internal.view.SupportMenu;

import java.lang.reflect.Array;
import java.util.Random;

public class FinalMaskBitmap {
    public static float ANIMATED_FRAME = 30.0f;
    public static int ANIMATED_FRAME_CAL = ((int) (ANIMATED_FRAME - 1.0f));
    public static int ORIGANAL_FRAME = 8;
    public static int TOTAL_FRAME = 30;
    static final Paint paint = new Paint();
    static int[][] randRect = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{20, 20}));
    static Random random = new Random();

    public enum EFFECT {
        CIRCLE_LEFT_TOP("CIRCLE LEFT TOP") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle(0.0f, 0.0f, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_RIGHT_TOP("Circle right top") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle((float) w, 0.0f, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_LEFT_BOTTOM("Circle left bottom") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle(0.0f, (float) h, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_RIGHT_BOTTOM("Circle right bottom") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle((float) w, (float) h, (((float) Math.sqrt((double) ((w * w) + (h * h)))) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_IN("Circle in") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint(1);
                paint.setColor(-16777216);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float r = FinalMaskBitmap.getRad(w * 2, h * 2);
                float f = (r / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                paint.setColor(-16777216);
                canvas.drawColor(-16777216);
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, r - f, paint);
                drawText(canvas);
                return mask;
            }
        },
        CIRCLE_OUT("Circle out") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, (FinalMaskBitmap.getRad(w * 2, h * 2) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor), paint);
                drawText(canvas);
                return mask;
            }
        },
        CROSS_IN("Cross in") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float fx = (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float fy = (((float) h) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(fx, 0.0f);
                path.lineTo(fx, fy);
                path.lineTo(0.0f, fy);
                path.lineTo(0.0f, 0.0f);
                path.close();
                path.moveTo((float) w, 0.0f);
                path.lineTo(((float) w) - fx, 0.0f);
                path.lineTo(((float) w) - fx, fy);
                path.lineTo((float) w, fy);
                path.lineTo((float) w, 0.0f);
                path.close();
                path.moveTo((float) w, (float) h);
                path.lineTo(((float) w) - fx, (float) h);
                path.lineTo(((float) w) - fx, ((float) h) - fy);
                path.lineTo((float) w, ((float) h) - fy);
                path.lineTo((float) w, (float) h);
                path.close();
                path.moveTo(0.0f, (float) h);
                path.lineTo(fx, (float) h);
                path.lineTo(fx, ((float) h) - fy);
                path.lineTo(0.0f, ((float) h) - fy);
                path.lineTo(0.0f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        CROSS_OUT("Cross out") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float fx = (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float fy = (((float) h) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo((((float) w) / 2.0f) + fx, 0.0f);
                path.lineTo((((float) w) / 2.0f) + fx, (((float) h) / 2.0f) - fy);
                path.lineTo((float) w, (((float) h) / 2.0f) - fy);
                path.lineTo((float) w, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) + fx, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) + fx, (float) h);
                path.lineTo((((float) w) / 2.0f) - fx, (float) h);
                path.lineTo((((float) w) / 2.0f) - fx, (((float) h) / 2.0f) - fy);
                path.lineTo(0.0f, (((float) h) / 2.0f) - fy);
                path.lineTo(0.0f, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        DIAMOND_IN("Diamond in") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Path path = new Path();
                float fx = (((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float fy = (((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                path.moveTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.lineTo((((float) w) / 2.0f) + fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        DIAMOND_OUT("Diamond out") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint(1);
                paint.setColor(-16777216);
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                canvas.drawColor(-16777216);
                Path path = new Path();
                float fx = ((float) w) - ((((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor));
                float fy = ((float) h) - ((((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor));
                path.moveTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.lineTo((((float) w) / 2.0f) + fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) + fy);
                path.lineTo((((float) w) / 2.0f) - fx, ((float) h) / 2.0f);
                path.lineTo(((float) w) / 2.0f, (((float) h) / 2.0f) - fy);
                path.close();
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        ECLIPSE_IN("Eclipse in") {
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float hf = (((float) h) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float wf = (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                RectF rectFL = new RectF(-wf, 0.0f, wf, (float) h);
                RectF rectFT = new RectF(0.0f, -hf, (float) w, hf);
                RectF rectFR = new RectF(((float) w) - wf, 0.0f, ((float) w) + wf, (float) h);
                RectF rectFB = new RectF(0.0f, ((float) h) - hf, (float) w, ((float) h) + hf);
                canvas.drawOval(rectFL, FinalMaskBitmap.paint);
                canvas.drawOval(rectFT, FinalMaskBitmap.paint);
                canvas.drawOval(rectFR, FinalMaskBitmap.paint);
                canvas.drawOval(rectFB, FinalMaskBitmap.paint);
                drawText(canvas);
                return mask;
            }
        },
        FOUR_TRIANGLE("Four triangle") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float ratioY = (((float) h) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ratioY);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(ratioX, 0.0f);
                path.lineTo((float) w, ((float) h) - ratioY);
                path.lineTo((float) w, (float) h);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.lineTo(0.0f, ratioY);
                path.close();
                path.moveTo(((float) w) - ratioX, 0.0f);
                path.lineTo((float) w, 0.0f);
                path.lineTo((float) w, ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(0.0f, (float) h);
                path.lineTo(0.0f, ((float) h) - ratioY);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        HORIZONTAL_RECT("Horizontal rect") {
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                float wf = ((float) w) / 10.0f;
                float rectW = (wf / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                for (int i = 0; i < 10; i++) {
                    canvas.drawRect(new Rect((int) (((float) i) * wf), 0, (int) ((((float) i) * wf) + rectW), h), paint);
                }
                drawText(canvas);
                return mask;
            }
        },
        HORIZONTAL_COLUMN_DOWNMASK("Horizontal column downmask") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float factorX = ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) / 2.0f;
                canvas.drawRoundRect(new RectF(0.0f, 0.0f, (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) / 2.0f)) * ((float) factor), ((float) h) / 2.0f), 0.0f, 0.0f, paint);
                if (((float) factor) >= 0.5f + factorX) {
                    canvas.drawRoundRect(new RectF(((float) w) - ((((float) w) / (((float) (FinalMaskBitmap.ANIMATED_FRAME_CAL - 1)) / 2.0f)) * ((float) ((int) (((float) factor) - factorX)))), ((float) h) / 2.0f, (float) w, (float) h), 0.0f, 0.0f, paint);
                }
                return mask;
            }
        },
        LEAF("LEAF") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setStrokeCap(Cap.BUTT);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                float fx = (float) ((w / FinalMaskBitmap.ANIMATED_FRAME_CAL) * factor);
                float fy = (float) ((h / FinalMaskBitmap.ANIMATED_FRAME_CAL) * factor);
                Canvas canvas = new Canvas(mask);
                Path path = new Path();
                path.moveTo(0.0f, (float) h);
                path.cubicTo(0.0f, (float) h, (((float) w) / 2.0f) - fx, (((float) h) / 2.0f) - fy, (float) w, 0.0f);
                path.cubicTo((float) w, 0.0f, (((float) w) / 2.0f) + fx, (((float) h) / 2.0f) + fy, 0.0f, (float) h);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        OPEN_DOOR("OPEN_DOOR") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setStrokeCap(Cap.BUTT);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                float fx = (float) ((w / FinalMaskBitmap.ANIMATED_FRAME_CAL) * factor);
                float f = (float) ((h / FinalMaskBitmap.ANIMATED_FRAME_CAL) * factor);
                Canvas canvas = new Canvas(mask);
                Path path = new Path();
                path.moveTo((float) (w / 2), 0.0f);
                path.lineTo(((float) (w / 2)) - fx, 0.0f);
                path.lineTo(((float) (w / 2)) - (fx / 2.0f), (float) (h / 6));
                path.lineTo(((float) (w / 2)) - (fx / 2.0f), (float) (h - (h / 6)));
                path.lineTo(((float) (w / 2)) - fx, (float) h);
                path.lineTo(((float) (w / 2)) + fx, (float) h);
                path.lineTo(((float) (w / 2)) + (fx / 2.0f), (float) (h - (h / 6)));
                path.lineTo(((float) (w / 2)) + (fx / 2.0f), (float) (h / 6));
                path.lineTo(((float) (w / 2)) + fx, 0.0f);
                path.lineTo(((float) (w / 2)) - fx, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return mask;
            }
        },
        PIN_WHEEL("PIN_WHEEL") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float rationX = (((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float rationY = (((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo(0.0f, (float) h);
                path.lineTo(rationX, (float) h);
                path.close();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo((float) w, (float) h);
                path.lineTo((float) w, ((float) h) - rationY);
                path.close();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo((float) w, 0.0f);
                path.lineTo(((float) w) - rationX, 0.0f);
                path.close();
                path.moveTo(((float) w) / 2.0f, ((float) h) / 2.0f);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(0.0f, rationY);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        RECT_RANDOM("RECT_RANDOM") {
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                float wf = (float) (w / FinalMaskBitmap.ANIMATED_FRAME_CAL);
                float hf = (float) (h / FinalMaskBitmap.ANIMATED_FRAME_CAL);
                for (int i = 0; i < FinalMaskBitmap.randRect.length; i++) {
                    int rand = FinalMaskBitmap.random.nextInt(FinalMaskBitmap.randRect[i].length);
                    while (FinalMaskBitmap.randRect[i][rand] == 1) {
                        rand = FinalMaskBitmap.random.nextInt(FinalMaskBitmap.randRect[i].length);
                    }
                    FinalMaskBitmap.randRect[i][rand] = 1;
                    for (int j = 0; j < FinalMaskBitmap.randRect[i].length; j++) {
                        if (FinalMaskBitmap.randRect[i][j] == 1) {
                            canvas.drawRoundRect(new RectF(((float) i) * wf, ((float) j) * hf, (((float) i) + 1.0f) * wf, (((float) j) + 1.0f) * hf), 0.0f, 0.0f, paint);
                        }
                    }
                }
                drawText(canvas);
                return mask;
            }
        },
        SKEW_LEFT_MEARGE("SKEW_LEFT_MEARGE") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ratioY);
                path.lineTo(ratioX, 0.0f);
                path.lineTo(0.0f, 0.0f);
                path.close();
                path.moveTo(((float) w) - ratioX, (float) h);
                path.lineTo((float) w, ((float) h) - ratioY);
                path.lineTo((float) w, (float) h);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SKEW_LEFT_SPLIT("SKEW_LEFT_SPLIT") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ratioY);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(ratioX, 0.0f);
                path.lineTo((float) w, ((float) h) - ratioY);
                path.lineTo((float) w, (float) h);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.lineTo(0.0f, ratioY);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SKEW_RIGHT_SPLIT("SKEW_RIGHT_SPLIT") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(((float) w) - ratioX, 0.0f);
                path.lineTo((float) w, 0.0f);
                path.lineTo((float) w, ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(0.0f, (float) h);
                path.lineTo(0.0f, ((float) h) - ratioY);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SKEW_RIGHT_MEARGE("SKEW_RIGHT_MEARGE") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                float ratioY = (((float) h) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, ((float) h) - ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(0.0f, (float) h);
                path.close();
                path.moveTo(((float) w) - ratioX, 0.0f);
                path.lineTo((float) w, ratioY);
                path.lineTo((float) w, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SQUARE_IN("SQUARE_IN") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                float ratioX = (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float ratioY = (((float) h) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(0.0f, (float) h);
                path.lineTo(ratioX, (float) h);
                path.lineTo(ratioX, 0.0f);
                path.moveTo((float) w, (float) h);
                path.lineTo((float) w, 0.0f);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.moveTo(ratioX, ratioY);
                path.lineTo(ratioX, 0.0f);
                path.lineTo(((float) w) - ratioX, 0.0f);
                path.lineTo(((float) w) - ratioX, ratioY);
                path.moveTo(ratioX, ((float) h) - ratioY);
                path.lineTo(ratioX, (float) h);
                path.lineTo(((float) w) - ratioX, (float) h);
                path.lineTo(((float) w) - ratioX, ((float) h) - ratioY);
                canvas.drawPath(path, paint);
                return mask;
            }
        },
        SQUARE_OUT("SQUARE_OUT") {
            public Bitmap getMask(int w, int h, int factor) {
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                float ratioX = (((float) w) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                float ratioY = (((float) h) / (((float) FinalMaskBitmap.ANIMATED_FRAME_CAL) * 2.0f)) * ((float) factor);
                new Canvas(mask).drawRect(new RectF(((float) (w / 2)) - ratioX, ((float) (h / 2)) - ratioY, ((float) (w / 2)) + ratioX, ((float) (h / 2)) + ratioY), paint);
                return mask;
            }
        },
        VERTICAL_RECT("VERTICAL_RECT") {
            public Bitmap getMask(int w, int h, int factor) {
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                float hf = ((float) h) / 10.0f;
                float rectH = (((float) factor) * hf) / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL);
                for (int i = 0; i < 10; i++) {
                    canvas.drawRect(new Rect(0, (int) (((float) i) * hf), w, (int) ((((float) i) * hf) + rectH)), paint);
                }
                drawText(canvas);
                return mask;
            }
        },
        WIND_MILL("WIND_MILL") {
            public Bitmap getMask(int w, int h, int factor) {
                float r = FinalMaskBitmap.getRad(w, h);
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                RectF oval = new RectF();
                oval.set((((float) w) / 2.0f) - r, (((float) h) / 2.0f) - r, (((float) w) / 2.0f) + r, (((float) h) / 2.0f) + r);
                float angle = (90.0f / ((float) FinalMaskBitmap.ANIMATED_FRAME_CAL)) * ((float) factor);
                canvas.drawArc(oval, 90.0f, angle, true, paint);
                canvas.drawArc(oval, 180.0f, angle, true, paint);
                canvas.drawArc(oval, 270.0f, angle, true, paint);
                canvas.drawArc(oval, 360.0f, angle, true, paint);
                drawText(canvas);
                return mask;
            }
        };

        String name;

        public abstract Bitmap getMask(int i, int i2, int i3);

        private EFFECT(String name2) {
            this.name = "";
            this.name = name2;
        }

        public void drawText(Canvas canvas) {
            Paint paint = new Paint();
            paint.setTextSize(50.0f);
            paint.setColor(SupportMenu.CATEGORY_MASK);
        }
    }

    static {
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
    }

    public static void reintRect() {
        randRect = (int[][]) Array.newInstance(Integer.TYPE, new int[]{(int) ANIMATED_FRAME, (int) ANIMATED_FRAME});
        for (int i = 0; i < randRect.length; i++) {
            for (int j = 0; j < randRect[i].length; j++) {
                randRect[i][j] = 0;
            }
        }
    }

    public static Paint getPaint() {
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        return paint;
    }

    static float getRad(int w, int h) {
        return (float) Math.sqrt((double) (((w * w) + (h * h)) / 4));
    }

    static Bitmap getHorizontalColumnDownMask(int w, int h, int factor) {
        Paint paint2 = new Paint();
        paint2.setColor(-16777216);
        paint2.setAntiAlias(true);
        paint2.setStyle(Style.FILL_AND_STROKE);
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        float factorX = ((float) ANIMATED_FRAME_CAL) / 2.0f;
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, (((float) w) / (((float) ANIMATED_FRAME_CAL) / 2.0f)) * ((float) factor), ((float) h) / 2.0f), 0.0f, 0.0f, paint2);
        if (((float) factor) >= 0.5f + factorX) {
            canvas.drawRoundRect(new RectF(((float) w) - ((((float) w) / (((float) (ANIMATED_FRAME_CAL - 1)) / 2.0f)) * ((float) ((int) (((float) factor) - factorX)))), ((float) h) / 2.0f, (float) w, (float) h), 0.0f, 0.0f, paint2);
        }
        return mask;
    }

    static Bitmap getRandomRectHoriMask(int w, int h, int factor) {
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        Paint paint2 = new Paint();
        paint2.setColor(-16777216);
        paint2.setAntiAlias(true);
        paint2.setStyle(Style.FILL_AND_STROKE);
        float wf = ((float) w) / 10.0f;
        float rectW = factor == 0 ? 0.0f : (((float) factor) * wf) / 9.0f;
        for (int i = 0; i < 10; i++) {
            canvas.drawRect(new Rect((int) (((float) i) * wf), 0, (int) ((((float) i) * wf) + rectW), h), paint2);
        }
        return mask;
    }

    static Bitmap getNewMask(int w, int h, int factor) {
        Paint paint2 = new Paint();
        paint2.setColor(-16777216);
        paint2.setAntiAlias(true);
        paint2.setStyle(Style.FILL_AND_STROKE);
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        float ratioX = (((float) w) / 18.0f) * ((float) factor);
        float ratioY = (((float) h) / 18.0f) * ((float) factor);
        Path path = new Path();
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) + ratioX, (float) (h / 2));
        path.lineTo((float) w, 0.0f);
        path.lineTo((float) (w / 2), ((float) (h / 2)) - ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) - ratioX, (float) (h / 2));
        path.lineTo(0.0f, 0.0f);
        path.lineTo((float) (w / 2), ((float) (h / 2)) - ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) - ratioX, (float) (h / 2));
        path.lineTo(0.0f, (float) h);
        path.lineTo((float) (w / 2), ((float) (h / 2)) + ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) + ratioX, (float) (h / 2));
        path.lineTo((float) w, (float) h);
        path.lineTo((float) (w / 2), ((float) (h / 2)) + ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.close();
        canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, (((float) w) / 18.0f) * ((float) factor), paint2);
        canvas.drawPath(path, paint2);
        return mask;
    }

    public static Bitmap getRadialBitmap(int w, int h, int factor) {
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        if (factor != 0) {
            Canvas canvas = new Canvas(mask);
            if (factor == 9) {
                canvas.drawColor(-16777216);
            } else {
                paint.setStyle(Style.STROKE);
                float radius = getRad(w, h);
                paint.setStrokeWidth((radius / 80.0f) * ((float) factor));
                for (int i = 0; i < 11; i++) {
                    canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, (radius / 10.0f) * ((float) i), paint);
                }
            }
        }
        return mask;
    }
}
