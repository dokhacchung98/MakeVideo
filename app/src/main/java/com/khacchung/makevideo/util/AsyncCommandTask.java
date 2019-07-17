package com.khacchung.makevideo.util;

import android.os.AsyncTask;
import android.util.Log;
//
//import com.arthenica.mobileffmpeg.FFmpeg;
//import com.arthenica.mobileffmpeg.util.RunCallback;
//
//public class AsyncCommandTask extends AsyncTask<String, Integer, Integer> {
//
//    private final RunCallback runCallback;
//    private static final String TAG = AsyncCommandTask.class.getName();
//
//    public AsyncCommandTask(final RunCallback runCallback) {
//        this.runCallback = runCallback;
//    }
//
//    @Override
//    protected Integer doInBackground(final String... arguments) {
//        return FFmpeg.execute(arguments[0], " ");
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
//        Log.e(TAG, "Size value: " + values.length + ", Value: " + values);
//    }
//
//    @Override
//    protected void onPostExecute(final Integer rc) {
//        if (runCallback != null) {
//            runCallback.apply(rc);
//        }
//    }
//
//    public static void executeAsync(final RunCallback runCallback, final String arguments) {
//        final AsyncCommandTask asyncCommandTask = new AsyncCommandTask(runCallback);
//        asyncCommandTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arguments);
//    }
//}
