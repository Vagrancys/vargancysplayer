package com.vargancys.vargancysplayer.module.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.vargancys.vargancysplayer.base.BaseActivity;
import com.vargancys.vargancysplayer.R;
import com.vargancys.vargancysplayer.module.home.HomePagerAdapter;
import com.vargancys.vargancysplayer.module.search.SearchActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.common_tab)
    CommonTabLayout commonTab;
    @BindView(R.id.main_search)
    TextView mainSearch;
    @BindView(R.id.main_game)
    ImageView mainGame;
    @BindView(R.id.main_history)
    ImageView mainHistory;

    private int[] mIconUnSelectId = {
            R.drawable.main_home,R.drawable.home_local,
            R.drawable.home_down,R.drawable.home_my
    };

    private int[] mIconSelectId = {
            R.drawable.main_home_select,R.drawable.home_local_select,
            R.drawable.home_down_select,R.drawable.home_my_select
    };

    private String[] mTitle = {
            "本地视频","本地音乐","网络视频","网络音乐"
    };

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private HomePagerAdapter mHomePager;
    private static boolean mExitFlag = false;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
        initTab();
        initPager();
    }

    public void initTab(){
        for (int j = 0,length = mTitle.length;j <length;j++){
            mTabEntities.add(new MainTabEntity(mTitle[j],mIconSelectId[j],mIconUnSelectId[j]));
        }
    }

    private void initPager(){
        mHomePager = new HomePagerAdapter(getSupportFragmentManager(),mTitle.length);

        viewPager.setOffscreenPageLimit(mTitle.length);
        viewPager.setAdapter(mHomePager);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                commonTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        commonTab.setTabData(mTabEntities);
        commonTab.setCurrentTab(0);
        commonTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    public class MainTabEntity implements CustomTabEntity{
        private String title;
        private int selectedIcon;
        private int unSelectedIcon;
        private MainTabEntity(String title,int selectedIcon,int unSelectedIcon){
            this.title=title;
            this.selectedIcon=selectedIcon;
            this.unSelectedIcon=unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            if(commonTab.getCurrentTab() != 0){
                commonTab.setCurrentTab(0);
                return true;
            }else if(!mExitFlag){
                Toast.makeText(MainActivity.this,"再按一次退出!",Toast.LENGTH_SHORT).show();
                mExitFlag = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mExitFlag = false;
                    }
                },2000);
                return true;
            }

        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                //Toast.makeText(getContext(),"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_game:
                Toast.makeText(getContext(),"游戏",Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_history:
                Toast.makeText(getContext(),"播放历史",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
