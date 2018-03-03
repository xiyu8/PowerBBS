package com.example.powerbbs.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.powerbbs.MainActivity;
import com.example.powerbbs.R;
import com.example.powerbbs.base.BaseActivity;
/*
* 更新介绍、欢迎界面
* */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    boolean firstLauch = false;
    private  final int GO_HOME = 500;
    private  final int GO_GUIDE = 501;
    private  final long SPLASH_DELAY_MILLIS = 1000;// 延迟1秒
    private void init() {
        // 读取SharedPreferences中需要的数据,使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = this.getSharedPreferences(getResources().getString(R.string.FirstLauch),MODE_PRIVATE);
//        SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        firstLauch = preferences.getBoolean(getResources().getString(R.string.FirstLauch), true);

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!firstLauch) {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }

    }

    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };



}
