package com.vargancys.vargancysplayer.module.home.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.Utils;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class HomeViewAdapter extends BaseRecyclerAdapter {
    private Context mContext;
    private ArrayList<MediaInfo> Medias;
    private int length;
    private Utils utils;

    public HomeViewAdapter(Context context, ArrayList<MediaInfo> medias) {
        this.mContext = context;
        this.Medias = medias;
        this.length = medias.size();
        this.utils = new Utils();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeViewHolder(View.inflate(mContext, R.layout.home_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeViewHolder mHolder = (HomeViewHolder) holder;
        final MediaInfo mediaInfo = Medias.get(position);
        mHolder.VideoTitle.setText(mediaInfo.getName());
        mHolder.VideoSize.setText(Formatter.formatFileSize(mContext,mediaInfo.getSize()));
        mHolder.VideoTime.setText(utils.stringForTime((int) mediaInfo.getDuration()));
    }

    @Override
    public int getItemCount() {
        return length;
    }

    class HomeViewHolder extends ViewHolder {
        @BindView(R.id.video_img)
        ImageView VideoImg;
        @BindView(R.id.video_title)
        TextView VideoTitle;
        @BindView(R.id.video_time)
        TextView VideoTime;
        @BindView(R.id.video_size)
        TextView VideoSize;
        HomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}
