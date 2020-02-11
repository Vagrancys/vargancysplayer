package com.vargancys.vargancysplayer.module.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.base.BaseActivity;
import com.vargancys.vargancysplayer.module.search.adapter.SearchViewAdapter;
import com.vargancys.vargancysplayer.module.search.data.SearchNetInfo;
import com.vargancys.vargancysplayer.utils.Constants;
import com.vargancys.vargancysplayer.utils.Https;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/11
 * version:1.0
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_voice)
    ImageView searchVoice;
    @BindView(R.id.search_text)
    TextView searchText;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;

    private SearchNetInfo searchNets;
    private List<SearchNetInfo.ItemsEntity> items = new ArrayList<>();
    private SearchViewAdapter mAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
        searchVoice.setOnClickListener(this);
        searchText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_voice:
                Toast.makeText(SearchActivity.this,"语音了！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.search_text:
                searchText();
                break;
        }
    }

    private void searchText(){
        String text = searchEdit.getText().toString().trim();
        if(!TextUtils.isEmpty(text)){
            if(items != null && items.size() > 0){
                items.clear();
            }
            try {
                text = URLEncoder.encode(text,"UTF-8");
                getDataFromNet(text);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDataFromNet(String uri){
        progressBar.setVisibility(View.VISIBLE);
        Https.getSearchNetAPI().getSearchNet(uri).enqueue(new Callback<SearchNetInfo>() {
            @Override
            public void onResponse(Call<SearchNetInfo> call, Response<SearchNetInfo> response) {
                searchNets = response.body();
                processData();
            }

            @Override
            public void onFailure(Call<SearchNetInfo> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this,"没有找到内容!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(){
        progressBar.setVisibility(View.GONE);
        items = searchNets.getItems();
        if(items != null && items.size() > 0){
            mAdapter = new SearchViewAdapter(this,items);
            recyclerView.setAdapter(mAdapter);
            tvNodata.setVisibility(View.GONE);
        }else{
            tvNodata.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }
}
