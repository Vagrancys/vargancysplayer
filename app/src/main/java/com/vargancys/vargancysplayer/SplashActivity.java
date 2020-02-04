package com.vargancys.vargancysplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;

import com.vargancys.vargancysplayer.base.BaseActivity;
import com.vargancys.vargancysplayer.module.common.MainActivity;

import butterknife.BindView;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/03
 * version:1.0
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.splash_relative)
    RelativeLayout splashRelative;

    private CountDownTimer mTimer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView(Bundle save) {
        mTimer = new CountDownTimer(1000,3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startMainActivity();
            }
        };

        mTimer.start();

        splashRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimer !=null){
                    mTimer.cancel();
                }
               startMainActivity();
            }
        });

        //通过handler来定时跳转
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        },3000);*/
    }

    private void startMainActivity(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
