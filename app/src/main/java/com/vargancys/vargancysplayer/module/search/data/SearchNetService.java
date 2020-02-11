package com.vargancys.vargancysplayer.module.search.data;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/11
 * version:1.0
 */
public interface SearchNetService {
    @GET()
    Call<SearchNetInfo> getSearchNet(String value);
}
