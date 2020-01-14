package com.zhufk.my_glide.library.cache;

import com.zhufk.my_glide.library.resource.Value;

/**
 * @ClassName MemoryCacheCallback
 * @Description 内存缓存中，元素被移除的接口回调
 * @Author zhufk
 * @Date 2019/12/11 16:40
 * @Version 1.0
 */
public interface MemoryCacheCallback {
    /**
     * 移除内存缓存中的 key-value
     * @param key
     * @param value
     */
    void entryRemovedMemoryCache(String key, Value value);
}
