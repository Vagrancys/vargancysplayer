package com.vargancys.vargancysplayer.module.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.Utils;
import com.vargancys.vargancysplayer.base.BaseActivity;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;
import com.vargancys.vargancysplayer.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/04
 * version:1.0
 */
public class VideoPlayerActivity extends BaseActivity implements View.OnClickListener {
    private static boolean isUseSystem = false;
    private static final int PROGRESS = 1;
    private static final int HIDE = 2;
    private static final String MEDIA = "media";
    private static final String POSITION = "position";
    private static final int SHOW_SPEED = 3;
    private static final int FULL_SCREEN = 1;
    private static final int DEFAULT_SCREEN = 2;

    @BindView(R.id.video)
    VideoView mVideo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_battery)
    ImageView ivBattery;
    @BindView(R.id.tv_system_time)
    TextView tvSystemTime;
    @BindView(R.id.btn_voice)
    Button btnVoice;
    @BindView(R.id.seek_voice)
    SeekBar seekVoice;
    @BindView(R.id.btn_switch)
    Button btnSwitch;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.seek_video)
    SeekBar seekVideo;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.btn_video_pre)
    Button btnVideoPre;
    @BindView(R.id.btn_video_start_pause)
    Button btnVideoStartPause;
    @BindView(R.id.btn_video_next)
    Button btnVideoNext;
    @BindView(R.id.btn_video_switch_screen)
    Button btnVideoSwitchScreen;
    @BindView(R.id.layout_relative)
    RelativeLayout LayoutRelative;
    @BindView(R.id.media_controller)
    RelativeLayout MediaController;
    @BindView(R.id.ll_buffer)
    LinearLayout LlBuffer;
    @BindView(R.id.buffer_title)
    TextView BufferTitle;
    @BindView(R.id.ll_loading)
    LinearLayout LlLoading;
    @BindView(R.id.loading_title)
    TextView LoadingTitle;
    private Uri uri;
    private Utils utils;
    private BatteryReceiver receiver;
    private ArrayList<MediaInfo> mediaItems;
    private int position;
    private boolean isFullScreen;
    private GestureDetector detector;
    private boolean isShowMediaController = false;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private int videoWidth;
    private int videoHeight;
    private AudioManager am;
    private int currentVoice;
    private int maxVoice;
    private boolean isMute = false;
    private boolean isNetUri;
    private static int preCurrentPosition;
    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
        initData();
        initClick();
        setListener();
        getData();
        setData();
    }



    private void getData(){
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaInfo>) getIntent().getSerializableExtra(MEDIA);
        position = getIntent().getIntExtra(POSITION,0);

    }

    private void setData(){
        if(mediaItems != null&& mediaItems.size() > 0){
            MediaInfo mediaInfo = mediaItems.get(position);
            tvTitle.setText(mediaInfo.getName());
            isNetUri = utils.isNetUri(mediaInfo.getData());
            mVideo.setVideoPath(mediaInfo.getData());
        }else if (uri != null) {
            tvTitle.setText(uri.toString());
            isNetUri = utils.isNetUri(uri.toString());
            mVideo.setVideoURI(uri);
        }else{
            Toast.makeText(VideoPlayerActivity.this,"没有传递数据",Toast.LENGTH_SHORT).show();
        }

        setButtonState();
    }

    private void initData(){
        utils = new Utils();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatteryReceiver();
        registerReceiver(receiver,intentFilter);

        detector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){


            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefault();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(isShowMediaController){
                    hideMediaController();
                    handler.removeMessages(HIDE);
                }else{
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE,4000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        //得到宽和高
        //screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        //screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        seekVoice.setMax(maxVoice);
        seekVoice.setProgress(currentVoice);

        handler.sendEmptyMessage(SHOW_SPEED);
    }

    private void setFullScreenAndDefault(){
        if(isFullScreen){
            setVideoType(FULL_SCREEN);
        }else{
            setVideoType(DEFAULT_SCREEN);
        }
    };

    private void setVideoType(int defaultScreen){
        switch (defaultScreen){
            case FULL_SCREEN:
                mVideo.setVideoSize(screenWidth,screenHeight);
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_default_selector);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
                int width = screenWidth;
                int height = screenHeight;
                if(mVideoHeight* height <width * mVideoWidth){
                    width = height * mVideoWidth / mVideoHeight;
                }else if(mVideoWidth * height > width * mVideoHeight){
                    height = width * mVideoHeight / mVideoWidth;
                }
                mVideo.setVideoSize(width,height);
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_full_selector);
                isFullScreen = false;
                break;
        }
    }

    private void startAndPause(){
        if(mVideo.isPlaying()){
            mVideo.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        }else{
            mVideo.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    private float startY;
    private float touchRang;
    private int mVol;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.min(screenHeight,screenWidth);
                handler.removeMessages(HIDE);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distanceY = startY -endY;
                float delta = (distanceY / touchRang) * maxVoice;
                int voice = (int) Math.min(Math.max(mVol+delta,0),maxVoice);
                if(delta != 0){
                    updateVoice(voice,false);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE,4000);
                break;
        }
        return super.onTouchEvent(event);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_SPEED:
                    String netSpeed = utils.getNetSpeed(VideoPlayerActivity.this);
                    LoadingTitle.setText("玩命加载中...."+netSpeed);
                    BufferTitle.setText("玩命缓存中....."+netSpeed);
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED,2000);
                    break;
                case HIDE:
                    hideMediaController();
                    break;
                case PROGRESS:
                    int currentPosition = mVideo.getCurrentPosition();
                    seekVideo.setProgress(currentPosition);
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    tvSystemTime.setText(getSystemTime());
                    if(isNetUri){
                        int buffer = mVideo.getBufferPercentage();
                        int totalBuffer = buffer * seekVideo.getMax();
                        int secondaryProgress = totalBuffer/100;
                        seekVideo.setSecondaryProgress(secondaryProgress);
                    }else{
                        seekVideo.setSecondaryProgress(0);
                    }

                    if(!isUseSystem&&mVideo.isPlaying()){
                        int buffer = currentPosition - preCurrentPosition;
                        if(buffer <500){
                            LlBuffer.setVisibility(View.VISIBLE);
                        }else{
                            LlBuffer.setVisibility(View.GONE);
                        }
                    }else{
                        LlBuffer.setVisibility(View.GONE);
                    }
                    preCurrentPosition = currentPosition;
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
            }
        }
    };

    //得到系统时间
    private String getSystemTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private void initClick(){

        btnExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);

        btnVideoNext.setOnClickListener(this);
        btnVideoSwitchScreen.setOnClickListener(this);

        LayoutRelative.setOnClickListener(this);
        btnVoice.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
    }

    private void setListener(){
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                mVideo.start();
                int duration = mVideo.getDuration();
                tvDuration.setText(utils.stringForTime(duration));
                seekVideo.setMax(duration);
                hideMediaController();
                handler.sendEmptyMessage(PROGRESS);
                setVideoType(DEFAULT_SCREEN);
                /*mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {

                    }
                });*/
                LlLoading.setVisibility(View.GONE);
            }
        });

        mVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoPlayerActivity.this, "错误", Toast.LENGTH_LONG).show();
                startVitamioPlayer();
                return false;
            }
        });

        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(VideoPlayerActivity.this, "播放完成了！", Toast.LENGTH_LONG).show();
                playNextVideo();
            }
        });

        if(isUseSystem){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                mVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what){
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                LlBuffer.setVisibility(View.VISIBLE);
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                LlBuffer.setVisibility(View.GONE);
                                break;
                        }
                        return true;
                    }
                });
            }
        }

        seekVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            /**
             * 当手指滑动的时候，会引起seekBar进度变化,会回调这个方法
             * @param seekBar
             * @param progress
             * @param fromUser 如果是用户引起的true，不是用户引起的false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mVideo.seekTo(progress);
                }
            }

            //当手指触碰的时候回调这个方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE);
            }

            //当手指离开的时候回调这个方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE,4000);
            }
        });

        seekVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(progress>0){
                        isMute = false;
                    }else{
                        isMute = true;
                    }
                    updateVoice(progress,false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(HIDE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.sendEmptyMessageDelayed(HIDE,4000);
            }
        });
    }

    //不能播放视频
    private void showErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("抱歉,无法播放该视频!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    //跳转万能播放器vitamio
    private void startVitamioPlayer(){
        if(mVideo != null){
            mVideo.stopPlayback();
        }
        finish();
    }

    //设置音量的大小
    private void updateVoice(int progress,boolean isMute) {
        if(isMute){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seekVoice.setProgress(0);
        }else{
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,1);
            seekVoice.setProgress(progress);
            currentVoice = progress;
        }

    }

    public static void launch(Activity activity, String data, ArrayList<MediaInfo> medias,int position) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.setDataAndType(Uri.parse(data), "video/*");
        Bundle bundle = new Bundle();
        bundle.putSerializable(MEDIA,medias);
        intent.putExtras(bundle);
        intent.putExtra(POSITION,position);
        activity.startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_voice:
                isMute = !isMute;
                updateVoice(currentVoice,isMute);
                break;
            case R.id.btn_switch:
                showSwitchPlayerDialog();
                break;
            case R.id.btn_video_start_pause:
                startAndPause();
                break;
            case R.id.btn_video_pre:
                playPreVideo();
                break;
            case R.id.btn_video_next:
                playNextVideo();
                break;
            case R.id.btn_exit:
                mVideo.pause();
                handler.removeMessages(PROGRESS);
                finish();
                break;
            case R.id.btn_video_switch_screen:
                setFullScreenAndDefault();
                break;
        }
        handler.removeMessages(HIDE);
        handler.sendEmptyMessageDelayed(HIDE,4000);
    }

    private void showSwitchPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当视频播放不正常时?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void playNextVideo(){
        if(mediaItems !=null && mediaItems.size()>0){
            position++;
            if(position <mediaItems.size()){
                LlLoading.setVisibility(View.VISIBLE);
                MediaInfo mediaInfo = mediaItems.get(position);
                tvTitle.setText(mediaInfo.getName());
                isNetUri = utils.isNetUri(mediaInfo.getData());
                mVideo.setVideoPath(mediaInfo.getData());
                setButtonState();
            }
        }else if(uri !=null){
            setButtonState();
        }
    }

    private void playPreVideo(){
        if(mediaItems !=null && mediaItems.size()>0){
            position--;
            if(position >= 0){
                LlLoading.setVisibility(View.VISIBLE);
                MediaInfo mediaInfo = mediaItems.get(position);
                tvTitle.setText(mediaInfo.getName());
                isNetUri = utils.isNetUri(mediaInfo.getData());
                mVideo.setVideoPath(mediaInfo.getData());
                setButtonState();
            }
        }else if(uri !=null){
            setButtonState();
        }
    }

    private void setButtonState(){
        if(mediaItems !=null && mediaItems.size()>0){
            if(mediaItems.size() == 1){
                setEnable(false);
            }else if(mediaItems.size() ==2){
                if(position == 0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoPre.setEnabled(false);
                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoNext.setEnabled(true);
                }else if(position == mediaItems.size() -1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);
                }
            } else {
                if(position == 0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoPre.setEnabled(false);
                }else if(position == mediaItems.size() -1){
                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }else{
                    setEnable(true);
                }
            }
        }else if(uri !=null){
          setEnable(false);
        }
    }

    private void setEnable(boolean isEnable){
        if(isEnable){
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoNext.setEnabled(true);
        }else{
            btnVideoPre.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }

    }

    class BatteryReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);
            setBattery(level);
        }
    }

    private void setBattery(int level){
        if(level <= 0){
            ivBattery.setImageResource(R.drawable.battery_0);
        }else if(level <= 20){
            ivBattery.setImageResource(R.drawable.battery_1);
        }else if(level <= 60){
            ivBattery.setImageResource(R.drawable.battery_2);
        }else if(level <=80){
            ivBattery.setImageResource(R.drawable.battery_3);
        }else if(level <=100){
            ivBattery.setImageResource(R.drawable.battery);
        }else{
            ivBattery.setImageResource(R.drawable.battery);
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }

        super.onDestroy();
    }

    private void hideMediaController(){
        isShowMediaController = false;
        MediaController.setVisibility(View.GONE);
    }

    private void showMediaController(){
        isShowMediaController = true;
        MediaController.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice--;
            updateVoice(currentVoice,false);
            handler.removeMessages(HIDE);
            handler.sendEmptyMessageDelayed(HIDE,4000);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice++;
            updateVoice(currentVoice,false);
            handler.removeMessages(HIDE);
            handler.sendEmptyMessageDelayed(HIDE,4000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
