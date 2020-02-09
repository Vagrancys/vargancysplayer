package com.vargancys.vargancysplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/09
 * version:1.0
 */
public class CacheUtils {
    public static void putString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("play",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("play",context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}
