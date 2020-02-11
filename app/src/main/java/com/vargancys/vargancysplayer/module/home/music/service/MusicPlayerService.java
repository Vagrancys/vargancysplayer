package com.vargancys.vargancysplayer.module.home.music.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import com.vargancys.vargancysplayer.IMusicPlayerService;
import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.module.common.MusicPlayerActivity;
import com.vargancys.vargancysplayer.module.home.data.MusicInfo;
import com.vargancys.vargancysplayer.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/10
 * version:1.0
 */
public class MusicPlayerService extends Service {
    public static String OPENAUDIO = "com.vargancys.mobileplayer_OPENAUDIO";
    private ArrayList<MusicInfo> musics;
    private int position;
    private MusicInfo musicInfo;
    private MediaPlayer mediaPlayer;
    private NotificationManager manager;
    //顺序播放
    public static final int REPEAT_NORMAL = 1;
    //单曲播放
    public static final int REPEAT_SINGLE = 2;
    //全部播放
    public static final int REPEAT_ALL = 3;
    //播放模式
    private int playmode = REPEAT_NORMAL;

    @Override
    public void onCreate() {
        super.onCreate();
        playmode = CacheUtils.getInt(this,"playmode");
        getDataFromLocal();
    }

    private void getDataFromLocal(){
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ContentResolver resolver = getContentResolver();

                    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    String[] objects = {
                            MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                            MediaStore.Audio.Media.DURATION,//视频总时长
                            MediaStore.Audio.Media.SIZE,//视频的文件大小
                            MediaStore.Audio.Media.DATA,//视频的绝对地址
                            MediaStore.Audio.Media.ARTIST//歌曲的演唱者
                    };
                    Cursor cursor = resolver.query(uri, objects, null, null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            MusicInfo musicInfo = new MusicInfo();
                            String name = cursor.getString(0);
                            musicInfo.setName(name);
                            long duration = cursor.getLong(1);
                            musicInfo.setDuration(duration);
                            long size = cursor.getLong(2);
                            musicInfo.setSize(size);
                            String data = cursor.getString(3);
                            musicInfo.setData(data);
                            String artist = cursor.getString(4);
                            musicInfo.setArtist(artist);
                            musics.add(musicInfo);
                        }
                        cursor.close();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {
            service.setPlayMode(playmode);
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            service.seekTo(progress);
        }

        @Override
        public int getAudioSessionId() throws RemoteException {
            return service.getAudioSessionId();
        }
    };

    //打开音乐
    private void openAudio(int position){
        this.position = position;
        if(musics !=null && musics.size()>0){
            musicInfo = musics.get(position);
            if(mediaPlayer !=null){
                //mediaPlayer.release();
                mediaPlayer.reset();
            }

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mediaPlayer.setDataSource(musicInfo.getData());
                mediaPlayer.prepareAsync();
                if(playmode == MusicPlayerService.REPEAT_SINGLE){
                    mediaPlayer.setLooping(true);
                }else{
                    mediaPlayer.setLooping(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(MusicPlayerService.this,"还没好!",Toast.LENGTH_SHORT).show();
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{
        @Override
        public void onPrepared(MediaPlayer mp) {
            EventBus.getDefault().post(musicInfo);
            start();
        }
    }

    //播放音乐
    private void start(){
        //notifyChange(OPENAUDIO);
        mediaPlayer.start();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,MusicPlayerActivity.class);
        intent.putExtra("notification",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.battery)
                .setContentTitle("流浪音乐")
                .setContentText("正在播放"+getName())
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1,notification);
    }

    private void notifyChange(String action){
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    //暂停音乐
    private void pause(){
        mediaPlayer.pause();
    }

    //停止音乐
    private void stop(){

    }

    /**
     * 得到当前的播放进度
     * @return
     */
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 得到当前音频的总时长
     * @return
     */
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * 得到艺术家的名称
     * @return
     */
    private String getArtist(){
        return musicInfo.getArtist();
    }

    /**
     * 得到歌曲名称
     * @return
     */
    private String getName(){
        return musicInfo.getName();
    }

    /**
     * 得到歌曲播放的路径
     * @return
     */
    private String getAudioPath(){
        return musicInfo.getData();
    }

    /**
     * 播放下一个音乐
     */
    private void next(){
        setNextPosition();
        openNextAudio();
    }

    private void setNextPosition(){
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            position++;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position++;
            if(position >= musics.size()){
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            position++;
            if(position >= musics.size()){
                position = 0;
            }
        }else{
            position++;
        }
    }

    private void openNextAudio(){
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            if(position < musics.size()){
                openAudio(position);
            }else{
                position = musics.size() - 1;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if(position < musics.size()){
                openAudio(position);
            }else{
                position = musics.size() - 1;
            }
        }
    }

    /**
     * 播放上一个音乐
     */
    private void pre(){
        setPrePosition();
        openPreAudio();
    }

    private void setPrePosition(){
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            position--;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position--;
            if(position < 0){
                position = musics.size() - 1;
            }
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            position--;
            if(position < 0){
                position = musics.size() - 1;
            }
        }else{
            position--;
        }
    }

    private void openPreAudio(){
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            if(position >= 0){
                openAudio(position);
            }else{
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }else{
            if(position >= 0){
                openAudio(position);
            }else{
                position = 0;
            }
        }
    }

    /**
     * 设置播放模式
     * @param playmode
     */
    private void setPlayMode(int playmode){
        this.playmode = playmode;
        CacheUtils.putInt(this,"playmode",1);
        if(playmode == MusicPlayerService.REPEAT_SINGLE){
            mediaPlayer.setLooping(true);
        }else{
            mediaPlayer.setLooping(false);
        }
    }

    /**
     * 得到播放模式
     * @return
     */
    private int getPlayMode(){
        return playmode;
    }

    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    private void seekTo(int progress){
        mediaPlayer.seekTo(progress);
    }

    private int getAudioSessionId(){
        return mediaPlayer.getAudioSessionId();
    }
}







