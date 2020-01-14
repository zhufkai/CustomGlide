package com.zhufk.my_glide.library.fragment;

/**
 * @ClassName LifeCycleCallback
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 21:52
 * @Version 1.0
 */
public interface LifeCycleCallback {
    void glideInitAction();

    void glideStopAction();

    void glideRecycleAction();
}
