package com.zhufk.my_glide.library;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * @ClassName Glide
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 20:44
 * @Version 1.0
 */

//疑问：1、Value 设置成单例 2、fragment声明周期监听时 Handler为什么发送空消息就会结束当前排队
public class Glide {

    private RequestManagerRetriever retriever;

    public Glide(RequestManagerRetriever retriever) {
        this.retriever = retriever;
    }

    public static RequestManager with(FragmentActivity fragmentActivity) {
        return getRetriever(fragmentActivity).get(fragmentActivity);
    }

    public static RequestManager with(Activity activity) {
        return getRetriever(activity).get(activity);
    }

    public static RequestManager with(Context context) {
        return getRetriever(context).get(context);
    }

    public static RequestManagerRetriever getRetriever(Context context) {
        return Glide.get(context).getRetriever();
    }

    public static Glide get(Context context){
        return new GlideBuilder().build();
    }

    public RequestManagerRetriever getRetriever(){
        return retriever;
    }
}
