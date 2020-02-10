package com.vargancys.vargancysplayer.module.home.netvideo.data;

import com.vargancys.vargancysplayer.module.home.data.MediaInfo;

import java.util.ArrayList;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/09
 * version:1.0
 */
public class NetVideoInfo {
    private int state;
    private ArrayList<MediaInfo> medias;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ArrayList<MediaInfo> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<MediaInfo> medias) {
        this.medias = medias;
    }
}
