package com.zhufk.my_glide.library;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.zhufk.my_glide.library.cache.ActiveCache;
import com.zhufk.my_glide.library.cache.DiskLruCacheImpl;
import com.zhufk.my_glide.library.cache.MemoryCache;
import com.zhufk.my_glide.library.cache.MemoryCacheCallback;
import com.zhufk.my_glide.library.fragment.LifeCycleCallback;
import com.zhufk.my_glide.library.load_data.LoadDataManager;
import com.zhufk.my_glide.library.load_data.ResourceListener;
import com.zhufk.my_glide.library.pool.BitmapPoolImpl;
import com.zhufk.my_glide.library.resource.Key;
import com.zhufk.my_glide.library.resource.Value;
import com.zhufk.my_glide.library.resource.ValueCallback;
import com.zhufk.my_glide.library.utils.Tools;

/**
 * @ClassName RequestTargetEngine
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 22:00
 * @Version 1.0
 */
public class RequestTargetEngine implements LifeCycleCallback, ValueCallback, MemoryCacheCallback, ResourceListener {

    private static final String TAG = RequestTargetEngine.class.getSimpleName();
    private final int MEMORY_MAX_SIZE = 1024 * 1024 * 60;
    private final int BITMAP_POOL_MAX_SIZE = 1024 * 1024 * 6;
    private Context context;
    //请求地址
    private String path;
    //加密的key
    private String key;
    //目标
    private ImageView imageView;

    @Override
    public void glideInitAction() {

    }

    @Override
    public void glideStopAction() {

    }

    @Override
    public void glideRecycleAction() {
        activeCache.closeThread();
    }

    private ActiveCache activeCache;//活动缓存
    private MemoryCache memoryCache;//内存缓存
    private DiskLruCacheImpl diskLruCache;//磁盘缓存
    private BitmapPoolImpl bitmapPool;

    public RequestTargetEngine() {
        if (activeCache == null) {
            activeCache = new ActiveCache(this);//回调告诉外界，Value不在使用
        }
        if (memoryCache == null) {
            memoryCache = new MemoryCache(MEMORY_MAX_SIZE);
            memoryCache.setMemoryCacheCallback(this);
        }
        diskLruCache = new DiskLruCacheImpl();
        bitmapPool = new BitmapPoolImpl(BITMAP_POOL_MAX_SIZE);
    }

    public void loadValueInitAction(String path, Context context) {
        this.context = context;
        this.path = path;
        this.key = new Key(path).getKey();
    }

    /**
     * 必须在主线程中
     *
     * @param imageView
     */
    public void into(ImageView imageView) {
        this.imageView = imageView;
        Tools.checkNotEmpty(imageView);
        Tools.assertMainThread();

        //加载资源
        Value value = cacheAction();
        if (value != null) {
            value.notUseAction();
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    private Value cacheAction() {
        //1、现在活动缓存中找
        Value value = activeCache.get(key);
        if (null != value) {
            Log.d(TAG, "activeCache 找到资源");
            value.useAction();
            return value;
        }
        //2、内存
        value = memoryCache.get(key);
        if (null != value) {
            memoryCache.manualRemove(key);//手动移除
            activeCache.put(key, value);
            Log.d(TAG, "memoryCache 找到资源");
            value.useAction();//计数
            return value;
        }
        //3、磁盘
        value = diskLruCache.get(key, bitmapPool);
        if (null != value) {
            activeCache.put(key, value);
            Log.d(TAG, "diskLruCache 找到资源");
            value.useAction();//计数
            return value;
        }
        //4、网络
        value = new LoadDataManager().loadResource(path, this, context);
        if (null != value) {
            return value;
        }
        return null;
    }

    @Override
    public void valueNotUseListener(String key, Value value) {
        if (null != key && null != value) {
            memoryCache.put(key, value);
        }
    }

    @Override
    public void entryRemovedMemoryCache(String key, Value value) {
        bitmapPool.put(value.getBitmap());
    }

    @Override
    public void responseSuccess(Value value) {
        if (null != value) {
            saveCache(key, value);
            imageView.setImageBitmap(value.getBitmap());
        }
    }

    @Override
    public void responseException(Exception e) {
        Log.e(TAG, "加载外部资源失败..." + e.getMessage());
    }

    private void saveCache(String key, Value value) {
        Log.d(TAG, "保存到磁盘缓存");
        value.setKey(key);
        if (null != diskLruCache) {
            diskLruCache.put(key, value);
        }
    }
}
