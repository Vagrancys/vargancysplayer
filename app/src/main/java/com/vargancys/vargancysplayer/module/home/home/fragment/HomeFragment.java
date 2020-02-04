package com.vargancys.vargancysplayer.module.home.home.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.base.BaseFragment;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.common.VideoPlayerActivity;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;
import com.vargancys.vargancysplayer.module.home.home.adapter.HomeViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_empty)
    TextView layoutEmpty;

    private ArrayList<MediaInfo> medias = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (medias != null && medias.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
                RecyclerViewData();
            } else {
                recyclerView.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        }
    };

    private HomeViewAdapter mHome;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void finishCreateView(Bundle save) {
        getDataFromLocal();
        mHome.setItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerView.ViewHolder holder) {
                Toast.makeText(getContext(),""+medias.get(position).getName(),Toast.LENGTH_LONG).show();
                startVideo(medias.get(position));
            }
        });
    }

    private void startVideo(MediaInfo media){
        //调用系统所有的播放器
        //Intent intent = new Intent();
        //intent.setDataAndType(Uri.parse(media.getData()),"video/*");
        //startActivity(intent);

        //调用自己的播放器
        VideoPlayerActivity.launch(getActivity(),media.getData());
    }

    private void getDataFromLocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ContentResolver resolver = getContext().getContentResolver();

                    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    String[] objects = {
                            MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                            MediaStore.Video.Media.DURATION,//视频总时长
                            MediaStore.Video.Media.SIZE,//视频的文件大小
                            MediaStore.Video.Media.DATA,//视频的绝对地址
                            MediaStore.Video.Media.ARTIST//歌曲的演唱者
                    };
                    Cursor cursor = resolver.query(uri, objects, null, null, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            MediaInfo mediaInfo = new MediaInfo();
                            String name = cursor.getString(0);
                            mediaInfo.setName(name);
                            long duration = cursor.getLong(1);
                            mediaInfo.setDuration(duration);
                            long size = cursor.getLong(2);
                            mediaInfo.setSize(size);
                            String data = cursor.getString(3);
                            mediaInfo.setData(data);
                            String artist = cursor.getString(4);
                            mediaInfo.setArtist(artist);
                            medias.add(mediaInfo);
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
        mHome = new HomeViewAdapter(getContext(),medias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(mHome);
    }
}