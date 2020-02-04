package com.vargancys.vargancysplayer;

import java.util.Formatter;
import java.util.Locale;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/04
 * version:1.0
 */
public class Utils {
    private StringBuffer mFormatBuilder;
    private Formatter mFormatter;

    public Utils(){
        mFormatBuilder = new StringBuffer();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    //把毫秒转换成 1：20：30形式
    public String stringForTime(int timeMs){
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes =(totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if(hours > 0){
            return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else{
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }
}
