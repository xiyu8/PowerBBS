package com.example.powerbbs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.powerbbs.base.ActivityCollector;
import com.example.powerbbs.base.BaseActivity;
import com.example.powerbbs.home.CommunicationMainAct;
import com.example.powerbbs.login.LoginActivity;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.powerbbs.tackle.NetRequest.*;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }



    public ImageView userMainIcon;
    public TextView drawername;
    public String app_url;
    public Button drawerlogin;
    public void initViews() {
        app_url = getResources().getString(R.string.app_url);
        drawername = findViewById(R.id.drawername);
        userMainIcon = (ImageView) findViewById(R.id.userMainIcon);
        drawerlogin = (Button) findViewById(R.id.tologin);
        Glide.with(this).load(R.drawable.icon)
                .bitmapTransform(new CropCircleTransformation(this))
//                .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .into((ImageView) findViewById(R.id.userMainIcon));
        //是否登陆了
        if(getLogin()) {
            Glide.with(this)
                    .load(app_url+"userIcon/" + getUserId()+".jpg")
                    // .override(100, 100) //图片大小
                    //.centerCrop()   //等比缩放
                    .bitmapTransform(new CropCircleTransformation(this))
                    .placeholder(R.drawable.icon) //
                    .error(R.drawable.icon)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userMainIcon);
            userMainIcon.setClickable(true);

        }else {
            userMainIcon.setClickable(false);
        }
    }





    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tologin:
                Intent in = new Intent(this, LoginActivity.class);
                startActivityForResult(in,1);
//                startActivity(in);
                break;
            case R.id.userMainIcon:
//                Intent in1 = new Intent(this, UserDetailActivity.class);
//                startActivityForResult(in1,2);
                    break;

            case R.id.content:
                Intent in1 = new Intent(this, CommunicationMainAct.class);
                startActivityForResult(in1,3);
                break;

            case R.id.finish:
                ActivityCollector.finishAll();  break;
            default: break;
        }
    }







    @Override  //login result and user detail info result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1: //login result
                if(resultCode==RESULT_OK){
                    String da=data.getStringExtra("name");
                    drawername.setText(da);
                    Glide.with(this)
                            .load(app_url+"userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                            //.override(100, 100) //图片大小
                            .centerCrop()   //等比缩放
                            //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                            .bitmapTransform(new CropCircleTransformation(this))
                            .placeholder(R.drawable.icon) //
                            .error(R.drawable.icon)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(userMainIcon);
                    drawerlogin.setText("切换账号");
                }else{
                }

                userMainIcon.setClickable(true);
                break;
            case 2: //after into user detail,and then back:if user change their info,then we will update in main activity.
                if(requestCode==RESULT_OK) {
                    if (getLogin()) {
                        Glide.with(MainActivity.this)
                                .load(app_url+"userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                                //.override(100, 100) //图片大小
                                .centerCrop()   //等比缩放
                                //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                                .bitmapTransform(new CropCircleTransformation(this))
                                .placeholder(R.drawable.icon) //
                                .error(R.drawable.icon)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(userMainIcon);

                        Glide.with(MainActivity.this)
                                .load(app_url+"userIcon/" + getUserId() + ".jpg"+"?"+System.currentTimeMillis())
                                .placeholder(R.drawable.icon)
                                .error(R.drawable.icon)
                                .into(userMainIcon);
                    }
                }
                break;
            default: break;
        }
//        if(getLogin()) {
//            Glide.with(this)
//                    .load("http://192.168.155.1/www/OpenJCU/userIcon/" + getUserId()+".jpg")
//                    .override(100, 100) //图片大小
//                    .centerCrop()   //等比缩放
//                    .bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
//                    .placeholder(R.drawable.icon) //
//                    .error(R.drawable.icon)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(userMainIcon);
//            userMainIcon.setClickable(true);
//
//        }else {
//            userMainIcon.setClickable(false);
//        }

        Glide.with(MainActivity.this)
                .load(app_url+"userIcon/" + getUserId()+".jpg"+"?"+System.currentTimeMillis())
                //.override(100, 100) //图片大小
                .centerCrop()   //等比缩放
                //.bitmapTransform(new BlurTransformation(this, 25), new CropCircleTransformation(this))
                .bitmapTransform(new CropCircleTransformation(this))
                .placeholder(R.drawable.icon) //
                .error(R.drawable.icon)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(userMainIcon);

    }





    //连按两次退出
    private long currentBackPressedTime = 0;          //第一次按得时间
    private final int BACK_PRESSED_INTERVAL = 2000;   //第一次与第二次间隔阈值
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis()- currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCollector.finishAll();
        }
    }


}
