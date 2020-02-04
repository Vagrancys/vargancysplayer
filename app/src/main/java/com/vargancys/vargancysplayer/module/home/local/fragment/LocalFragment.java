package com.vargancys.vargancysplayer.module.home.local.fragment;

import android.os.Bundle;

import com.vargancys.vargancysplayer.base.BaseFragment;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class LocalFragment extends BaseFragment {

    public static LocalFragment newInstance(){
        return new LocalFragment();
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void finishCreateView(Bundle save) {

    }
}
