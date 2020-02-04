package com.vargancys.vargancysplayer.module.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vargancys.vargancysplayer.module.home.down.fragment.DownFragment;
import com.vargancys.vargancysplayer.module.home.home.fragment.HomeFragment;
import com.vargancys.vargancysplayer.module.home.local.fragment.LocalFragment;
import com.vargancys.vargancysplayer.module.home.my.fragment.MyFragment;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class HomePagerAdapter extends FragmentPagerAdapter{

    private Fragment[] mFragment;
    private int Count;

    public HomePagerAdapter(FragmentManager fm,int size){
        super(fm);
        Count = size;
        mFragment = new Fragment[Count];
    }
    @Override
    public Fragment getItem(int position) {
        if(mFragment[position] == null){
            switch (position){
                case 0:
                    mFragment[position] = HomeFragment.newInstance();
                    break;
                case 1:
                    mFragment[position] = LocalFragment.newInstance();
                    break;
                case 2:
                    mFragment[position] = DownFragment.newInstance();
                    break;
                case 3:
                    mFragment[position] = MyFragment.newInstance();
                    break;
                default:
                    break;
            }
        }

        return mFragment[position];
    }

    @Override
    public int getCount() {
        return Count;
    }
}
