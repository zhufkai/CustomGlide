package com.zhufk.my_glide.library.load_data;

import com.zhufk.my_glide.library.resource.Value;

/**
 * @ClassName ResourceListener
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/12 15:01
 * @Version 1.0
 */
public interface ResourceListener {
    void responseSuccess(Value value);

    void responseException(Exception e);
}
