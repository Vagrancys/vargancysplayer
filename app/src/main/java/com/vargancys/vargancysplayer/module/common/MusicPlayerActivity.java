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
import com.vargancys.vargancysplayer.module.home.data.MusicInfo;
import com.vargancys.vargancysplayer.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/04
 * version:1.0
 */
public class MusicPlayerActivity extends BaseActivity{

    private static final String MUSIC = "music";
    private static final String POSITION = "position";

    @Override
    public int getLayoutId() {
        return R.layout.activity_music;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
    }

    //得到系统时间
    private String getSystemTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    public static void launch(Activity activity, String data, ArrayList<MusicInfo> medias,int position) {
        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        intent.setDataAndType(Uri.parse(data), "video/*");
        Bundle bundle = new Bundle();
        bundle.putSerializable(MUSIC,medias);
        intent.putExtras(bundle);
        intent.putExtra(POSITION,position);
        activity.startActivity(intent);
    }

}
