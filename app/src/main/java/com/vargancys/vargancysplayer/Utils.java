package com.vargancys.vargancysplayer;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Message;

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
    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

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

    public boolean isNetUri(String uri){
        boolean reault = false;
        if(uri !=null){
            if(uri.toLowerCase().startsWith("http")||
                    uri.toLowerCase().startsWith("rtsp")||
                    uri.toLowerCase().startsWith("mms")){
                reault = true;
            }
        }
        return reault;
    }

    /**
     * 得到网络速度
     * @param context
     * @return
     */
    public String getNetSpeed(Context context){
        String netSpeed = "0 kb/s";
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED?0:(TrafficStats.getTotalRxBytes()/1024);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 /(nowTimeStamp - lastTimeStamp));

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;

        netSpeed = String.valueOf(speed +" kb/s");
        return netSpeed;
    }

}
