package com.vargancys.vargancysplayer.module.home.down.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.Utils;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/09
 * version:1.0
 */
public class NetVideoViewAdapter extends BaseRecyclerAdapter {
    private Context mContext;
    private ArrayList<MediaInfo> Medias;

    public NetVideoViewAdapter(Context context, ArrayList<MediaInfo> medias) {
        this.mContext = context;
        this.Medias = medias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NetVideoViewHolder(View.inflate(mContext, R.layout.net_video_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NetVideoViewHolder mHolder = (NetVideoViewHolder) holder;
        final MediaInfo mediaInfo = Medias.get(position);
        Glide.with(mContext)
                .load(mediaInfo.getImage())
                .placeholder(R.drawable.icon_video)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mHolder.VideoImg);
        mHolder.VideoTitle.setText(mediaInfo.getName());
        mHolder.VideoDesc.setText(mediaInfo.getDesc());
    }

    @Override
    public int getItemCount() {
        return Medias.size();
    }

    class NetVideoViewHolder extends ViewHolder {
        @BindView(R.id.video_img)
        ImageView VideoImg;
        @BindView(R.id.video_title)
        TextView VideoTitle;
        @BindView(R.id.video_desc)
        TextView VideoDesc;
        NetVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}