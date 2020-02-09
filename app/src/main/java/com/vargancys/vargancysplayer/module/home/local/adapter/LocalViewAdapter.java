package com.vargancys.vargancysplayer.module.home.local.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.Utils;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;
import com.vargancys.vargancysplayer.module.home.data.MusicInfo;
import com.vargancys.vargancysplayer.module.home.home.adapter.HomeViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/09
 * version:1.0
 */
public class LocalViewAdapter  extends BaseRecyclerAdapter {
    private Context mContext;
    private ArrayList<MusicInfo> Musics;
    private Utils utils;

    public LocalViewAdapter(Context context, ArrayList<MusicInfo> musics) {
        this.mContext = context;
        this.Musics = musics;
        this.utils = new Utils();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LocalViewHolder(View.inflate(mContext, R.layout.local_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalViewHolder mHolder = (LocalViewHolder) holder;
        final MusicInfo musicInfo = Musics.get(position);
        mHolder.MusicTitle.setText(musicInfo.getName());
        mHolder.MusicSize.setText(Formatter.formatFileSize(mContext,musicInfo.getSize()));
        mHolder.MusicTime.setText(utils.stringForTime((int) musicInfo.getDuration()));
    }

    @Override
    public int getItemCount() {
        return Musics.size();
    }

    class LocalViewHolder extends ViewHolder {
        @BindView(R.id.music_title)
        TextView MusicTitle;
        @BindView(R.id.music_time)
        TextView MusicTime;
        @BindView(R.id.music_size)
        TextView MusicSize;
        LocalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}