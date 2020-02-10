package com.vargancys.vargancysplayer.module.home.music.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.base.BaseFragment;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.common.MusicPlayerActivity;
import com.vargancys.vargancysplayer.module.home.data.MusicInfo;
import com.vargancys.vargancysplayer.module.home.music.adapter.MusicViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class MusicFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_empty)
    TextView layoutEmpty;
    private ArrayList<MusicInfo> musics = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (musics != null && musics.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
                RecyclerViewData();
            } else {
                recyclerView.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        }
    };

    private MusicViewAdapter mLocal;

    public static MusicFragment newInstance(){
        return new MusicFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    public void finishCreateView(Bundle save) {
        getDataFromLocal();
        mLocal.setItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerView.ViewHolder holder) {
                //Toast.makeText(getContext(),""+musics.get(position).getName(),Toast.LENGTH_LONG).show();
                startVideo(musics.get(position),position);
            }
        });
    }

    private void startVideo(MusicInfo media,int position){
        //调用系统所有的音乐播放器
        //Intent intent = new Intent();
        //intent.setDataAndType(Uri.parse(media.getData()),"video/*");
        //startActivity(intent);

        //调用自己的音乐播放器
        MusicPlayerActivity.launch(getActivity(),media.getData(),musics,position);
    }


    private void getDataFromLocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ContentResolver resolver = getContext().getContentResolver();

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
                    mHandler.sendEmptyMessage(10);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void RecyclerViewData(){
        mLocal = new MusicViewAdapter(getContext(),musics);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(mLocal);
    }
}
