package com.zhufk.my_glide.library.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;

/**
 * @ClassName ActivityFragmentManager
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 21:16
 * @Version 1.0
 */
public class ActivityFragmentManager extends Fragment {

    private LifeCycleCallback callback;

    @SuppressLint("ValidFragment")
    public ActivityFragmentManager(LifeCycleCallback callback){
        this.callback = callback;
    }

    public ActivityFragmentManager(){}

    @Override
    public void onStart() {
        super.onStart();
        if (callback !=null){
            callback.glideInitAction();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (callback !=null){
            callback.glideStopAction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callback !=null){
            callback.glideRecycleAction();
        }
    }
}
