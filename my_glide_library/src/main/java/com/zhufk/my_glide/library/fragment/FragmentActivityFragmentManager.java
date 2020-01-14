package com.zhufk.my_glide.library.fragment;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

/**
 * @ClassName FragmentActivityFragmentManager
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 21:15
 * @Version 1.0
 */
public class FragmentActivityFragmentManager extends Fragment {
    private LifeCycleCallback callback;

    @SuppressLint("ValidFragment")
    public FragmentActivityFragmentManager(LifeCycleCallback callback){
        this.callback =callback;
    }

    public FragmentActivityFragmentManager(){}

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
