package com.zhufk.my_glide.library.pool;

import android.graphics.Bitmap;

/**
 * @ClassName BitmapPool
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/12 20:43
 * @Version 1.0
 */
public interface BitmapPool {

    void put(Bitmap bitmap);

    Bitmap get(int w, int h, Bitmap.Config config);

}
