package com.vargancys.vargancysplayer.module.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vargancys.vargancysplayer.IMusicPlayerService;
import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.Utils;
import com.vargancys.vargancysplayer.base.BaseActivity;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;
import com.vargancys.vargancysplayer.module.home.data.MusicInfo;
import com.vargancys.vargancysplayer.module.home.music.service.MusicPlayerService;
import com.vargancys.vargancysplayer.utils.LyricUtils;
import com.vargancys.vargancysplayer.widget.BaseVisualizerView;
import com.vargancys.vargancysplayer.widget.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/04
 * version:1.0
 */
public class MusicPlayerActivity extends BaseActivity implements View.OnClickListener{

    private static final String MUSIC = "music";
    private static final String POSITION = "position";
    private static final int PROGRESS = 1;
    private static final int SHOW_LYRIC = 2;

    @BindView(R.id.iv_icon)
    BaseVisualizerView ivIcon;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seek_music)
    SeekBar seekMusic;
    @BindView(R.id.btn_playmode)
    Button btnPlaymode;
    @BindView(R.id.btn_music_pre)
    Button btnMusicPre;
    @BindView(R.id.btn_music_start_pause)
    Button btnMusicStartPause;
    @BindView(R.id.btn_music_next)
    Button btnMusicNext;
    @BindView(R.id.btn_music_word)
    Button btnMusicWord;
    @BindView(R.id.showLyricView)
    ShowLyricView showLyricView;

    private boolean notification;
    private Utils utils;
    private MyReceiver receiver;
    private int position = 0;
    private IMusicPlayerService service;
    private Visualizer mVisualizer;
    private ServiceConnection con = new ServiceConnection() {
        /**
         * 当连接成功的时候回调这个方法
         * @param name
         * @param
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);
            if (service != null) {
                try {
                    if(!notification){
                        //从列表
                        service.openAudio(position);
                    }else{
                        //从状态栏
                        showViewData();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当连接断开的时候回调这个方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (service != null) {
                    service.stop();
                    service = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_music;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
        initData();
        getData();
        bindAndStartService();
        seekMusic.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private void initData(){
        utils = new Utils();
        //receiver = new MyReceiver();
        //IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction(MusicPlayerService.OPENAUDIO);
        //registerReceiver(receiver,intentFilter);
        //1.eventBus注册
        EventBus.getDefault().register(this);
        setListener();
    }

    private void setupVisualizerFxAndUi(){
        try {
            int audioSessionId = service.getAudioSessionId();
            mVisualizer = new Visualizer(audioSessionId);
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            ivIcon.setVisualizer(mVisualizer);
            mVisualizer.setEnabled(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setListener(){
        btnMusicPre.setOnClickListener(this);
        btnMusicStartPause.setOnClickListener(this);
        btnPlaymode.setOnClickListener(this);
        btnMusicNext.setOnClickListener(this);
    }

    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            showData(null);
        }
    }

    //3.eventBus 订阅方法
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)
    public void showData(MusicInfo musicInfo){
        showLyric();

        showViewData();
        checkPlayMode();
        setupVisualizerFxAndUi();
    }

    private void showLyric(){
        LyricUtils lyricUtils =  new LyricUtils();
        try {
            String path = service.getAudioPath();
            path = path.substring(0,path.lastIndexOf("."));
            File file = new File(path + ".lrc");
            if(!file.exists()){
                file = new File(path + ".txt");
            }
            lyricUtils.readLyricFile(file);
            showLyricView.setLyrics(lyricUtils.getLyrics());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(lyricUtils.isExistLyric()){
            handler.sendEmptyMessage(SHOW_LYRIC);
        }
    }

    private void showViewData(){
        try {
            tvAuthor.setText(service.getArtist());
            tvTitle.setText(service.getName());
            seekMusic.setMax(service.getDuration());
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_LYRIC:
                    //显示歌词
                    try {
                        int currentPosition = service.getCurrentPosition();
                        showLyricView.setShowNextLyric(currentPosition);
                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case PROGRESS:
                    try {
                        int currentPosition = service.getCurrentPosition();
                        seekMusic.setProgress(currentPosition);
                        tvTime.setText(utils.stringForTime(currentPosition)+"/"+utils.stringForTime(service.getDuration()));
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS,1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.vargancys.mobileplayer_OPENAUDIO");
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification", false);
        if(!notification){
            position = getIntent().getIntExtra(POSITION, 0);
        }
    }

    public static void launch(Activity activity, String data, ArrayList<MusicInfo> medias, int position) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        intent.setDataAndType(Uri.parse(data), "video/*");
        Bundle bundle = new Bundle();
        bundle.putSerializable(MUSIC, medias);
        intent.putExtras(bundle);
        intent.putExtra(POSITION, position);
        activity.startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_playmode:
                setPlayMode();
                break;
            case R.id.btn_music_pre:
                if(service !=null){
                    try {
                        service.pre();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_music_start_pause:
                if(service !=null){
                    try {
                        if(service.isPlaying()){
                            service.pause();
                            btnMusicStartPause.setBackgroundResource(R.drawable.btn_music_start_selector);
                        }else{
                            service.start();
                            btnMusicStartPause.setBackgroundResource(R.drawable.btn_music_pause_selector);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_music_next:
                if(service !=null){
                    try {
                        service.next();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void setPlayMode() {
        try {
            int playmode = service.getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
                playmode = MusicPlayerService.REPEAT_SINGLE;
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                playmode = MusicPlayerService.REPEAT_ALL;
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }else{
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }
            service.setPlayMode(playmode);
            showPlayMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showPlayMode(){
        try {
            int playmode = service.getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
                Toast.makeText(MusicPlayerActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_single_selector);
                Toast.makeText(MusicPlayerActivity.this,"单曲播放",Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_all_selector);
                Toast.makeText(MusicPlayerActivity.this,"全部播放",Toast.LENGTH_SHORT).show();
            }else{
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
                Toast.makeText(MusicPlayerActivity.this,"顺序播放",Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void checkPlayMode(){
        try {
            int playmode = service.getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_single_selector);
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_all_selector);
            }else{
                btnPlaymode.setBackgroundResource(R.drawable.btn_playmode_normal_selector);
            }
            //校验播放暂停的状态
            if(service.isPlaying()){
                btnPlaymode.setBackgroundResource(R.drawable.btn_music_start_selector);
            }else{
                btnPlaymode.setBackgroundResource(R.drawable.btn_music_pause_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        /*if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }*/
        //2.eventBus 取消注册
        EventBus.getDefault().unregister(this);
        if(con !=null){
            unbindService(con);
            con = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVisualizer != null){
            mVisualizer.release();
        }
    }
}
