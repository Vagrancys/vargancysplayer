package com.vargancys.vargancysplayer.utils;

import com.vargancys.vargancysplayer.module.home.netvideo.data.NetVideoService;
import com.vargancys.vargancysplayer.module.search.data.SearchNetService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/09
 * version:1.0
 */
public class Https {
    public static NetVideoService getNetVideoAPI(){
        return createAPI(NetVideoService.class,Constants.HTTP);
    }

    public static SearchNetService getSearchNetAPI(){
        return createAPI(SearchNetService.class,Constants.NET_SEARCH_URI);
    }

    private static <T> T createAPI (Class<T> clazz,String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(clazz);
    }
}
