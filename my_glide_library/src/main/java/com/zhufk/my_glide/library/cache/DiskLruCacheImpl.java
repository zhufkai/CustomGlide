package com.zhufk.my_glide.library.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.zhufk.my_glide.library.cache.disk.DiskLruCache;
import com.zhufk.my_glide.library.pool.BitmapPool;
import com.zhufk.my_glide.library.resource.Value;
import com.zhufk.my_glide.library.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ClassName DiskLruCacheImpl
 * @Description 磁盘缓存 -- 封装diskLruCache
 * @Author zhufk
 * @Date 2019/12/11 17:19
 * @Version 1.0
 */
public class DiskLruCacheImpl {
    private final String TAG = DiskLruCacheImpl.class.getSimpleName();
    //sd/disk_lru_cache_dir/
    private final String DISK_LRU_CACHE_DIR = "disk_lru_cache_dir";
    private final int APP_VERSION = 1;//版本号，一旦修改版本号，之前的缓存会被清除
    private final int VALUE_COUNT = 1;
    private final long MAX_SIZE = 1024 * 1024 * 10;
    private DiskLruCache diskLruCache;

    public DiskLruCacheImpl() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + DISK_LRU_CACHE_DIR);
        try {
            diskLruCache = DiskLruCache.open(file, APP_VERSION, VALUE_COUNT, MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, Value value) {
        DiskLruCache.Editor edit = null;
        OutputStream outputStream = null;
        try {
            edit = diskLruCache.edit(key);
            outputStream = edit.newOutputStream(0);//index 不能大于 VALUE_COUNT
            Bitmap bitmap = value.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                edit.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
                Log.e(TAG, "put: edit.abort() e:" + e.getMessage());
            }
        } finally {
            try {
                edit.commit();
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "put: edit.commit() e:" + e.getMessage());
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "put: outputStream.close() e:" + e.getMessage());
                }
            }
        }
    }

    public Value get(String key, BitmapPool bitmapPool) {
        Tools.checkNotEmpty(key);
        InputStream inputStream = null;
        Value value = Value.getInstance();
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                inputStream = snapshot.getInputStream(0);//index 不能大于 VALUE_COUNT

                //TODO 临时定义
                int w = 200;
                int h = 200;

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmapPoolResult = bitmapPool.get(w, h, Bitmap.Config.RGB_565);
                options.inBitmap = bitmapPoolResult;
                options.inMutable = true;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                //TODO 可以追加设置
//                options.inSampleSize =

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                value.setBitmap(bitmap);
                value.setKey(key);
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                value.setBitmap(bitmap);
//                value.setKey(key);
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "get: inputStream.close() e:" + e.getMessage());
                }
            }
        }
        return null;
    }
}
