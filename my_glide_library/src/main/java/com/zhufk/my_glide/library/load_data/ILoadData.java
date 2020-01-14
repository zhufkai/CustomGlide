package com.zhufk.my_glide.library.load_data;

import android.content.Context;

import com.zhufk.my_glide.library.resource.Value;

/**
 * @ClassName ILoadData
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/12 15:00
 * @Version 1.0
 */
public interface ILoadData {
    Value loadResource(String path, ResourceListener listener, Context context);
}
