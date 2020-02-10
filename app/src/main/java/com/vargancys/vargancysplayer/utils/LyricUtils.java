package com.vargancys.vargancysplayer.utils;

import com.vargancys.vargancysplayer.module.home.data.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/10
 * version:1.0
 * 解析歌词工具类
 */
public class LyricUtils {
    private ArrayList<Lyric> lyrics;
    /**
     * 读取歌词文件
     * @param file /mnt/scard/audio/beijing.lrc
     */
    public void readLyricFile(File file){
        if(file == null || file.exists()){
            lyrics = null;
        }else{
            lyrics = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
                String line = "";
                while (true){
                    if (((line = reader.readLine()) != null)){
                        line = parsedLyric(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //解析一句歌词
    private String parsedLyric(String line) {
        //indexOf第一次出现的位置
        int pos1 = line.indexOf("[");//0,如果没有返回-1
        int pos2 = line.indexOf("]");//9,如果没有返回-1
        if(pos1 == 0&& pos2 != -1){
            long[] time = new long[getCountTag(line)];
            String strTime = line.substring(pos1+1,pos2);
            time[0] = strTimeToLongTime(strTime);
        }
        return null;
    }

    //把string类型时间转换成龙类型
    private long strTimeToLongTime(String strTime) {

        return 0;
    }

    //判断多少句歌词
    private int getCountTag(String line) {
        String[] left = line.split("\\[");
        String[] right = line.split("\\]");
        int result = -1;
        if(left.length ==0&& right.length==0){
            result = 1;
        }else if(left.length>right.length){
            result = left.length;
        }else{
            result = right.length;
        }
        return result;
    }

}
