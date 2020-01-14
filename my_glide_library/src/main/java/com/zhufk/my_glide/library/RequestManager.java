package com.zhufk.my_glide.library;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.zhufk.my_glide.library.fragment.ActivityFragmentManager;
import com.zhufk.my_glide.library.fragment.FragmentActivityFragmentManager;

/**
 * @ClassName RequestManager
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 20:48
 * @Version 1.0
 */
public class RequestManager {

    private final String FRAGMENT_ACTIVITY_NAME = "Fragment_Activity_Name";
    private final String ACTIVITY_NAME = "Activity_Name";
    private final int NEXT_HANDLER_MSG = 999444;
    private Context requestManagerContext;

    private RequestTargetEngine engine;

    {
        if (engine == null) {
            engine = new RequestTargetEngine();
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });

    /**
     * 可以管理生命周期
     *
     * @param fragmentActivity
     */
    public RequestManager(FragmentActivity fragmentActivity) {
        this.requestManagerContext = fragmentActivity;
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME);
        if (null == fragment) {
            fragment = new FragmentActivityFragmentManager(engine);
            supportFragmentManager.beginTransaction().add(fragment, FRAGMENT_ACTIVITY_NAME).commitAllowingStateLoss();
        }

        handler.sendEmptyMessage(NEXT_HANDLER_MSG);
    }

    /**
     * 可以管理生命周期
     *
     * @param activity
     */
    public RequestManager(Activity activity) {
        this.requestManagerContext = activity;
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(ACTIVITY_NAME);
        if (null == fragment) {
            fragment = new ActivityFragmentManager(engine);
            fragmentManager.beginTransaction().add(fragment, ACTIVITY_NAME).commitAllowingStateLoss();
        }

        handler.sendEmptyMessage(NEXT_HANDLER_MSG);
    }

    /**
     * 无法管理生命周期
     *
     * @param context
     */
    public RequestManager(Context context) {
        this.requestManagerContext = context;
    }

    /**
     * 显示的图片路径
     *
     * @param path
     */
    public RequestTargetEngine load(String path) {
        handler.removeMessages(NEXT_HANDLER_MSG);
        engine.loadValueInitAction(path, requestManagerContext);
        return engine;
    }
}
