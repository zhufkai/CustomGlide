package com.zhufk.my_glide.library;

/**
 * @ClassName GlideBuilder
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 20:56
 * @Version 1.0
 */
public class GlideBuilder {

    public Glide build(){
        RequestManagerRetriever retriever =new RequestManagerRetriever();
        Glide glide = new Glide(retriever);
        return glide;
    }
}
