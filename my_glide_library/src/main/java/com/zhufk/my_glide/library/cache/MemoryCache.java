package com.zhufk.my_glide.library.cache;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import com.zhufk.my_glide.library.resource.Value;

/**
 * @ClassName MemoryCache
 * @Description 内存缓存
 * @Author zhufk
 * @Date 2019/12/11 16:13
 * @Version 1.0
 */
public class MemoryCache extends LruCache<String, Value> {

    //是否是手动缓存
    private boolean isManualRemove;

    private MemoryCacheCallback callback;

    public void setMemoryCacheCallback(MemoryCacheCallback callback) {
        this.callback = callback;
    }

    /**
     * 手动移除
     */
    public Value manualRemove(String key) {
        isManualRemove = true;
        Value value = remove(key);
        isManualRemove = false;
        return value;
    }

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public MemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Value value) {
//        return super.sizeOf(key, value);
        Bitmap bitmap = value.getBitmap();

        //old 最早版本
//        int result = bitmap.getRowBytes() * bitmap.getHeight();
        //new 12 3.0
//        result = bitmap.getByteCount();//在bitmap复用中有区别（所属的）
        //API 19 4.4
//        result = bitmap.getAllocationByteCount();//在bitmap复用中有区别（整体的）
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getByteCount();
    }

    /**
     * 1.重复的key
     * 2.最少使用的元素会被移除
     *
     * @param evicted
     * @param key
     * @param oldValue
     * @param newValue
     */
    @Override
    protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Value oldValue, @Nullable Value newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        if (null != callback && !isManualRemove) {
            callback.entryRemovedMemoryCache(key, oldValue);
        }
    }
}
