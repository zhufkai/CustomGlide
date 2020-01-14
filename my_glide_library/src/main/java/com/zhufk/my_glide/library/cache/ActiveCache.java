package com.zhufk.my_glide.library.cache;

import android.util.Log;

import com.zhufk.my_glide.library.resource.Value;
import com.zhufk.my_glide.library.resource.ValueCallback;
import com.zhufk.my_glide.library.utils.Tools;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ActiveCache
 * @Description 活动缓存
 * @Author zhufk
 * @Date 2019/12/11 13:50
 * @Version 1.0
 */
public class ActiveCache {
    private final String TAG = ActiveCache.class.getSimpleName();
    private Map<String, WeakReference<Value>> mapList = new HashMap<>();
    private ReferenceQueue<Value> queue;
    private boolean isCloseThread;//是否关闭线程
    private Thread thread;
    private boolean isManualRemove;//手动移除-- false：被动移除  true: 手动移除
    private ValueCallback valueCallback;

    public ActiveCache(ValueCallback valueCallback) {
        this.valueCallback = valueCallback;
    }

    public void put(String key, Value value) {
        Tools.checkNotEmpty(key);
        Tools.checkNotEmpty(value);
        value.setCallback(valueCallback);
        mapList.put(key, new CustomWeakReference(value, getQueue(), key));
    }

    public Value get(String key) {
        WeakReference<Value> valueWeakReference = mapList.get(key);
        if (null != valueWeakReference) {
            return valueWeakReference.get();
        }
        return null;
    }

    /**
     * 手动移除
     *
     * @param key
     * @return
     */
    public Value manualRemove(String key) {
        isManualRemove = true;
        WeakReference<Value> valueWeakReference = mapList.remove(key);
        isManualRemove = false;
        if (null != valueWeakReference) {
            return valueWeakReference.get();
        }
        return null;
    }

    /**
     * 关闭线程
     */
    public void closeThread() {
        isCloseThread = true;
//        if (null != thread) {
//            thread.interrupt();
//            try {
//                thread.join(TimeUnit.SECONDS.toMillis(5));
//                if (thread.isAlive()) {
//                    throw new IllegalStateException("活动缓存中，关闭线程 线程没有停止下来...");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        mapList.clear();
        System.gc();
    }

    /**
     * 监听移除  被动
     */
    public class CustomWeakReference extends WeakReference<Value> {

        String key;

        public CustomWeakReference(Value referent, ReferenceQueue<? super Value> q, String key) {
            super(referent, q);
            this.key = key;
        }
    }

    /**
     * 监听软引用
     *
     * @return
     */
    private ReferenceQueue<Value> getQueue() {
        if (queue == null) {
            queue = new ReferenceQueue<>();

            thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (!isCloseThread) {
                        try {
                            //isManualRemove 如果不进行判断手动移除也会走进这个方法
                            if (!isManualRemove) {
                                //阻塞式方法 等待GC
                                Reference<? extends Value> remove = queue.remove();
                                CustomWeakReference weakReference = (CustomWeakReference) remove;
                                Log.d(TAG, "active-->remove : " + mapList.size());
                                if (mapList != null && !mapList.isEmpty()) {
                                    mapList.remove(weakReference.key);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
        return queue;
    }
}
