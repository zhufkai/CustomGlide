package com.zhufk.my_glide.library.resource;

import android.graphics.Bitmap;
import android.util.Log;

import com.zhufk.my_glide.library.utils.Tools;

/**
 * @ClassName Value
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 13:49
 * @Version 1.0
 */
public class Value {
    private final String TAG = Value.class.getCanonicalName();

    private static Value value;

    private Value(){}

    public static Value getInstance() {
        if (null == value) {
            synchronized (Value.class) {
                if (null == value) {
                    value = new Value();
                }
            }
        }
        return value;
    }

    private Bitmap bitmap;

    private int count;

    private ValueCallback callback;

    private String key;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ValueCallback getCallback() {
        return callback;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCallback(ValueCallback valueCallback) {
        this.callback = valueCallback;
    }

    public void useAction() {
        Tools.checkNotEmpty(bitmap);

        if (bitmap.isRecycled()) {
            Log.d(TAG, "useAction-->count:" + count);
            return;
        }
        Log.d(TAG, "useAction 加一 count:" + count);
        count++;
    }

    public void notUseAction() {
        count--;
        if (count <= 0 && callback != null) {
            Log.d(TAG, "notUseAction -->count:" + count);
            callback.valueNotUseListener(key, this);
        }
        Log.d(TAG, "useAction 减一 count:" + count);
    }

    public void recycleBitmap() {
        if (count > 0) {
            Log.d(TAG, "不能被释放");
            return;
        }
        if (bitmap.isRecycled()) {
            return;
        }

        bitmap.recycle();
        System.gc();
    }
}
