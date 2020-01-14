package com.zhufk.my_glide.library;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * @ClassName RequestManagerRetriever
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 20:52
 * @Version 1.0
 */
public class RequestManagerRetriever {
    public RequestManager get(FragmentActivity fragmentActivity) {
        return new RequestManager(fragmentActivity);
    }

    public RequestManager get(Activity activity) {
        return new RequestManager(activity);
    }

    public RequestManager get(Context context) {
        return new RequestManager(context);
    }
}
