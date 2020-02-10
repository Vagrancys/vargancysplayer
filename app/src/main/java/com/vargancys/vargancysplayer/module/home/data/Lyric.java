package com.vargancys.vargancysplayer.module.home.data;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/10
 * version:1.0
 */
public class Lyric {
    //歌词内容
    private String content;
    //时间戳
    private long timePoint;
    //休眠时间
    private long sleepTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(long timePoint) {
        this.timePoint = timePoint;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
}
