package com.vargancys.vargancysplayer.module.common;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.base.BaseActivity;

import butterknife.BindView;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/04
 * version:1.0
 */
public class VideoPlayerActivity extends BaseActivity {
    @BindView(R.id.video)
    VideoView mVideo;
    private Uri uri;
    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
        uri = getIntent().getData();
        if(uri != null){
            mVideo.setVideoURI(uri);
        }

        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideo.start();
            }
        });

        mVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoPlayerActivity.this,"错误",Toast.LENGTH_LONG).show();
                return false;
            }
        });

        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoPlayerActivity.this,"播放完成了！",Toast.LENGTH_LONG).show();
            }
        });

        mVideo.setMediaController(new MediaController(this));
    }

    public static void launch(Activity activity, String data){
        Intent intent = new Intent(activity,VideoPlayerActivity.class);
        intent.setDataAndType(Uri.parse(data),"video/*");
        activity.startActivity(intent);
    }
}
