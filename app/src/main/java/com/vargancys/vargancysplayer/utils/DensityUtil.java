package com.vargancys.vargancysplayer.utils;

import android.content.Context;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/11
 * version:1.0
 */
public class DensityUtil {
    public static int dip2px(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale +0.5f);
    }

    public static int px2dip(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale +0.5f);
    }
}
