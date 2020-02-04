package com.vargancys.vargancysplayer.base;

import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public Unbinder bind;
    public abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        initToolbar();
        initView(savedInstanceState);
    }

    public Context getContext(){
        return this;
    }

    public abstract void initToolbar();

    public abstract void initView(Bundle save);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();

    }
}
