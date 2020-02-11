package com.vargancys.vargancysplayer.utils;

import com.vargancys.vargancysplayer.module.home.data.Lyric;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/10
 * version:1.0
 * 解析歌词工具类
 */
public class LyricUtils {
    private ArrayList<Lyric> lyrics;

    //得到解析好的歌词
    public ArrayList<Lyric> getLyrics() {
        return lyrics;
    }

    private boolean isExistLyric = false;

    //是否存在歌词
    public boolean isExistLyric() {
        return isExistLyric;
    }

    /**
     * 读取歌词文件
     * @param file /mnt/scard/audio/beijing.lrc
     */
    public void readLyricFile(File file){
        if(file == null || file.exists()){
            lyrics = null;
            isExistLyric = false;
        }else{
            lyrics = new ArrayList<>();
            isExistLyric = true;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),codeString(file)));
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
            Collections.sort(lyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric lhs, Lyric rhs) {
                    if(lhs.getTimePoint() < rhs.getTimePoint()){
                        return -1;
                    }else if(lhs.getTimePoint() > rhs.getTimePoint()){
                        return 1;
                    }else{
                        return 0;
                    }
                }
            });

            for (int i = 0;i< lyrics.size();i++){
                Lyric oneLyric = lyrics.get(i);
                if(i+1 <lyrics.size()){
                    Lyric twoLyric = lyrics.get(i+1);
                    oneLyric.setSleepTime(twoLyric.getTimePoint()-oneLyric.getTimePoint());
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
            long[] times = new long[getCountTag(line)];
            String strTime = line.substring(pos1+1,pos2);
            times[0] = strTimeToLongTime(strTime);

            String content = line;
            int i = 1;
            while (pos1 == 0 && pos2 != -1){
                content = content.substring(pos2+1);
                pos1 = content.indexOf("[");
                pos2 = content.indexOf("]");
                if(pos2 != -1){
                    strTime = content.substring(pos1+1,pos2);
                    times[i] = strTimeToLongTime(strTime);
                    if(times[i] == -1){
                        return "";
                    }
                    i++;
                }
            }
            Lyric lyric = new Lyric();
            for (int j = 0;j<times.length;j++){
                if(times[j] != 0){
                    lyric.setContent(content);
                    lyric.setTimePoint(times[j]);
                    lyrics.add(lyric);
                    lyric = new Lyric();
                }
            }
            return content;
        }
        return "";
    }

    //把string类型时间转换成long类型
    private long strTimeToLongTime(String strTime) {
        long result = -1;
        try {
            String[] s1 = strTime.split(":");
            String[] s2 = s1[1].split("\\.");
            //分
            long min = Long.parseLong(s1[0]);
            //秒
            long second = Long.parseLong(s2[0]);
            //毫秒
            long mil = Long.parseLong(s2[1]);
            result = min * 60 *1000 + second * 1000 + mil *10;
        }catch (Exception e){
            e.printStackTrace();
            result = -1;
        }
        return result;
    }

    /**
     * 判断文件的编码格式
     * @param fileName :file
     * @return 文件编码格式
     * @throws
     */
    public static String codeString(File file) throws Exception{
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        //其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            case 0x5c75:
                code = "ANSI|ASCII" ;
                break ;
            default:
                code = "GBK";
        }

        return code;
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
