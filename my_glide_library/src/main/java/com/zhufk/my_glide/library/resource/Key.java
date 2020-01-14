package com.zhufk.my_glide.library.resource;

import com.zhufk.my_glide.library.utils.Tools;

/**
 * @ClassName Key
 * @Description TODO
 * @Author zhufk
 * @Date 2019/12/11 13:49
 * @Version 1.0
 */
public class Key {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Key(String key){
        this.key = Tools.getSHA256StrJava(key);
    }

}
