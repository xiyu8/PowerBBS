package com.example.powerbbs.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.powerbbs.R;
import com.example.powerbbs.tackle.Mog;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);     //每个Activity都添加进ActivityCollector，方便管理所有活动
        Mog.e(getString(R.string.app_name),getClass().getSimpleName());
//        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
