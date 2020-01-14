package com.zhufk.my_glide.library.pool;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import java.util.TreeMap;

/**
 * @ClassName BitmapPoolImpl
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/12 22:22
 * @Version 1.0
 */
public class BitmapPoolImpl extends LruCache<Integer, Bitmap> implements BitmapPool {
    private final String TAG = BitmapPool.class.getSimpleName();

    private TreeMap<Integer, String> treeMap = new TreeMap<>();

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapPoolImpl(int maxSize) {
        super(maxSize);
    }

    @Override
    public void put(Bitmap bitmap) {
        if (!bitmap.isMutable()) {
            Log.d(TAG, "条件一：isMutable 为 true 不满足");
            return;
        }

        int bitmapSize = getBitmapSize(bitmap);
        if (bitmapSize >= maxSize()) {
            Log.d(TAG, "条件二不满足 bitmapSize 不能大于 maxSize");
            return;
        }

        put(bitmapSize, bitmap);

        treeMap.put(bitmapSize, null);
    }

    private int getBitmapSize(Bitmap bitmap) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }

        return bitmap.getByteCount();
    }

    @Override
    public Bitmap get(int w, int h, Bitmap.Config config) {
        int bitmapSize = w * h * (config == Bitmap.Config.ARGB_8888 ? 4 : 2);
        Integer key = treeMap.ceilingKey(bitmapSize);//返回大于等于bitmapSize的key
        if (null == key) {
            return null;
        }

        //TODO
        if (key < (bitmapSize * 2)) {
            Bitmap bitmap = remove(key);
            return bitmap;
        }
        return null;
    }

    @Override
    protected int sizeOf(@NonNull Integer key, @NonNull Bitmap value) {
//        return super.sizeOf(key, value);
        return getBitmapSize(value);
    }

    @Override
    protected void entryRemoved(boolean evicted, @NonNull Integer key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
    }
}
