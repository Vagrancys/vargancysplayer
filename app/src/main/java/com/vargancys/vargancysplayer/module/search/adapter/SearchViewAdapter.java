package com.vargancys.vargancysplayer.module.search.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;
import com.vargancys.vargancysplayer.module.search.data.SearchNetInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/09
 * version:1.0
 */
public class SearchViewAdapter extends BaseRecyclerAdapter {
    private Context mContext;
    private List<SearchNetInfo.ItemsEntity> Items;

    public SearchViewAdapter(Context context, List<SearchNetInfo.ItemsEntity> items) {
        this.mContext = context;
        this.Items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NetVideoViewHolder(View.inflate(mContext, R.layout.net_video_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NetVideoViewHolder mHolder = (NetVideoViewHolder) holder;
        final SearchNetInfo.ItemsEntity itemInfo = Items.get(position);
        Glide.with(mContext)
                .load(itemInfo.getItemImages().get(0).getItemUri())
                .placeholder(R.drawable.icon_video)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mHolder.VideoImg);
        mHolder.VideoTitle.setText(itemInfo.getItemTitle());
        mHolder.VideoDesc.setText(itemInfo.getKeywords());
    }

    @Override
    public int getItemCount() {
        return Items.size();
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