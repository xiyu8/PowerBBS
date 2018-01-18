package com.example.powerbbs.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 晞余 on 2018/1/18.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
