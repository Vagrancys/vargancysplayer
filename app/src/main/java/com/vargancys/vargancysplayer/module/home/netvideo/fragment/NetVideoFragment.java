package com.vargancys.vargancysplayer.module.home.netvideo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.base.BaseFragment;
import com.vargancys.vargancysplayer.base.BaseRecyclerAdapter;
import com.vargancys.vargancysplayer.module.common.VideoPlayerActivity;
import com.vargancys.vargancysplayer.module.home.data.MediaInfo;
import com.vargancys.vargancysplayer.module.home.netvideo.adapter.NetVideoViewAdapter;
import com.vargancys.vargancysplayer.module.home.netvideo.data.NetVideoInfo;
import com.vargancys.vargancysplayer.utils.Https;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class NetVideoFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_empty)
    TextView layoutEmpty;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout SwipeLayout;

    private ArrayList<MediaInfo> netVideo = new ArrayList<>();
    private NetVideoViewAdapter mNet;
    private boolean isData = false;
    private Context mContext;
    public static NetVideoFragment newInstance(){
        return new NetVideoFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_net_video;
    }

    @Override
    public void finishCreateView(Bundle save) {
        mContext = getContext();
        SwipeLayout.setColorSchemeColors(getResources().getColor(R.color.pink));

        LazyLoadingData();

        setListener();
    }

    private void setListener(){
        SwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SwipeLayout.setRefreshing(true);
                getDataFormNet();
            }
        });

        mNet.setItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerView.ViewHolder holder) {
                Toast.makeText(getContext(),""+netVideo.get(position).getName(),Toast.LENGTH_LONG).show();
                startVideo(netVideo.get(position),position);
            }
        });
    }

    private void LazyLoadingData(){
        if(!isData){
            SwipeLayout.setRefreshing(true);
            /*String saveJson = CacheUtils.getString(mContext,Constants.NET_URI);
            if(!TextUtils.isEmpty(saveJson)){

            }*/
            getDataFormNet();
        }
    }

    private void startVideo(MediaInfo media, int position){
        VideoPlayerActivity.launch(getActivity(),media.getData(),netVideo,position);
    }

    private void getDataFormNet(){
        Https.getNetVideoAPI()
                .getNetVideo()
                .enqueue(new Callback<NetVideoInfo>() {
                    @Override
                    public void onResponse(Call<NetVideoInfo> call, Response<NetVideoInfo> response) {
                        if(response.body() !=null&&response.body().getState()==200){
                            //CacheUtils.putString(mContext, Constants.NET_URI,response.body().toString());
                            netVideo.clear();
                            netVideo = response.body().getMedias();
                            if (netVideo != null && netVideo.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layoutEmpty.setVisibility(View.GONE);
                                RecyclerViewData();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                layoutEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NetVideoInfo> call, Throwable t) {
                        SwipeLayout.setRefreshing(false);
                        recyclerView.setVisibility(View.GONE);
                        layoutEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void RecyclerViewData(){
        SwipeLayout.setRefreshing(false);
        mNet = new NetVideoViewAdapter(getActivity(),netVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(mNet);
    }
}
