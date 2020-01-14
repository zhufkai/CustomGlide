package com.zhufk.my_glide.library.utils;

import android.graphics.Bitmap;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.zhufk.my_glide.library.resource.Value;

import java.security.MessageDigest;

/**
 * @ClassName Tools
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 15:04
 * @Version 1.0
 */
public class Tools {
    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String tem = null;
        for (int i = 0; i < bytes.length; i++) {
            tem = Integer.toHexString(bytes[i] & 0xFF);
            if (tem.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(tem);
        }
        return stringBuffer.toString();
    }

    public static void checkNotEmpty(Bitmap bitmap) {
        if (null == bitmap) {
            throw new IllegalArgumentException("Must not be empty, 传递进来的bitmap：" + bitmap + "是null");
        }
    }

    public static void checkNotEmpty(ImageView imageView) {
        if (null == imageView) {
            throw new IllegalArgumentException("Must not be empty, 传递进来的imageView：" + imageView + "是null");
        }
    }

    public static void checkNotEmpty(Value value) {
        if (null == value) {
            throw new IllegalArgumentException("Must not be empty");
        }
    }

    public static String checkNotEmpty(String string) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException("Must not be empty");
        }
        return string;
    }

    public static void assertMainThread() {
        if (!isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread");
        }
    }

    private static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
