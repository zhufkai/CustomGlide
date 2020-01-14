package com.zhufk.my_glide.library.load_data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zhufk.my_glide.library.resource.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoadDataManager
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/12 15:02
 * @Version 1.0
 */
public class LoadDataManager implements ILoadData, Runnable {

    private static final String TAG = LoadDataManager.class.getSimpleName();
    private String path;
    private ResourceListener listener;
    private Context context;

    @Override
    public Value loadResource(String path, ResourceListener listener, Context context) {
        this.path = path;
        this.listener = listener;
        this.context = context;

        //TODO 加载本地图片

        //加载网络图片
        Uri uri = Uri.parse(path);

        if ("HTTP".equalsIgnoreCase(uri.getScheme()) || "HTTPS".equalsIgnoreCase(uri.getScheme())) {
            new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>()).execute(this);
        }

        return null;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(path);
            URLConnection urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(5000);
            final int responseCode = httpURLConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                inputStream = httpURLConnection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Value value = Value.getInstance();
                        Log.d(TAG, "value:" + value.getKey() + value.getBitmap());
                        value.setBitmap(bitmap);
                        listener.responseSuccess(value);
                    }
                });
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.responseException(new IllegalStateException("请求失败 请求码：" + responseCode));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
}
