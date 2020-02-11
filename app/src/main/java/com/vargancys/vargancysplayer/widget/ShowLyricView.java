package com.vargancys.vargancysplayer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vargancys.vargancysplayer.module.home.data.Lyric;
import com.vargancys.vargancysplayer.utils.DensityUtil;

import java.util.ArrayList;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/10
 * version:1.0
 */
public class ShowLyricView extends AppCompatTextView{
    private ArrayList<Lyric> lyrics = new ArrayList<>();
    private Paint paint;
    private Paint whitePaint;
    private int width;
    private int height;
    //歌词列表中的索引
    private int index;
    private float textHeight;
    //当前播放进度
    private float currentPosition;
    //高亮显示的时间
    private float sleepTime;
    //时间戳 什么时刻到高亮哪句歌词
    private float timePoint;

    public void setLyrics(ArrayList<Lyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ShowLyricView(Context context){
        this(context,null);
    }

    public ShowLyricView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public ShowLyricView(Context context,AttributeSet attrs,int defstyleAttr){
        super(context, attrs,defstyleAttr);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView(Context context){

        textHeight = DensityUtil.dip2px(context,20);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(DensityUtil.dip2px(context,20));
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        whitePaint = new Paint();
        whitePaint.setColor(Color.GREEN);
        whitePaint.setTextSize(DensityUtil.dip2px(context,20));
        whitePaint.setAntiAlias(true);
        whitePaint.setTextAlign(Paint.Align.CENTER);
        lyrics = new ArrayList<>();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lyrics !=null && lyrics.size()> 0){
            float plush = 0;
            if(sleepTime == 0){
                plush = 0;
            }else{
                //平移
                //这一句所花的时间：休眠时间 = 移动的距离 ：总距离(行高)
                //移动的距离
                plush = textHeight + ((currentPosition - timePoint) / sleepTime) * textHeight;
            }
            canvas.translate(0,-plush);
            String currentText = lyrics.get(index).getContent();
            canvas.drawText(currentText,width/2,height/2,paint);
            float tempY = height/2;
            for (int i = index-1;i>=0;i--){
                String preText = lyrics.get(i).getContent();
                tempY = tempY - textHeight;
                if(tempY < 0){
                    break;
                }
                canvas.drawText(preText,width/2,tempY,whitePaint);
            }
            tempY = height/2;
            for (int i = index+1;i < lyrics.size();i++){
                String nextText = lyrics.get(i).getContent();
                tempY = tempY + textHeight;
                if(tempY > height){
                    break;
                }
                canvas.drawText(nextText,width/2,tempY,whitePaint);
            }
        }else{
            canvas.drawText("没有歌词",width/2,height/2,paint);
        }
    }

    //根据当前播放的位置，找到高亮的歌词
    public void setShowNextLyric(int currentPosition) {
        this.currentPosition = currentPosition;
        if(lyrics == null || lyrics.size() == 0 ){
            return;
        }
        for (int i = 1; i < lyrics.size(); i++){
            if(currentPosition<lyrics.get(i).getTimePoint()){
                int tempIndex = i - 1;
                if(currentPosition >= lyrics.get(tempIndex).getTimePoint()){
                    //当前正在播放的歌词
                    index = tempIndex;
                    sleepTime = lyrics.get(index).getSleepTime();
                    timePoint = lyrics.get(index).getTimePoint();
                }
            }
        }
        invalidate();
    }
}
