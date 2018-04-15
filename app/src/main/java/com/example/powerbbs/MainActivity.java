package com.example.powerbbs;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.powerbbs.home.main_fragment.GroupFragment;
import com.example.powerbbs.home.main_fragment.HomeFragment;
import com.example.powerbbs.login.LoginActivity;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.example.powerbbs.tackle.NetRequest.*;

public class MainActivity extends BaseActivity {

    FragmentManager fragmentManager;
    HomeFragment homeFragment;
    GroupFragment groupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }



    public ImageView userMainIcon,homeImage,groupImage;
    public TextView drawername,homeText,groupText;
    public String app_url;
    public Button drawerlogin;
    public void initViews() {
        ////////////checkout fragment views//////////////
        homeImage = findViewById(R.id.home_image);
        homeText = findViewById(R.id.home_text);
        groupImage = findViewById(R.id.group_image);
        groupText = findViewById(R.id.group_text);

        /////////////////////////

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
                ////////////////////checkout fragment/////////////////////////////
            case R.id.group:
                setTabSelection(1);
                break;
            case R.id.in_home:
                setTabSelection(0);
                break;
                ////////////////////////////////////////////////
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



//////////////////////////////////////////////////about checkout fragment/////////////////////////////////////////////////////////////////////////////////


    /**
     * 根据传入的index参数来设置选中的tab页。
     * @param index
     *            每个tab页对应的下标。，1，2，3。
     */
    public void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态

        Log.e("OpenJCU", "$$$$$$$$$$$$$"+getLogin()+getUsername()+getPwd());
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                homeImage.setImageResource(R.drawable.icon);
                homeText.setTextColor(Color.parseColor("#47c6fc"));
                if (homeFragment == null) {
                    // 如果homeFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content,homeFragment);
                } else {
                    // 如果homeFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                groupImage.setImageResource(R.drawable.icon);
                groupText.setTextColor(Color.parseColor("#47c6fc"));
                if (groupFragment == null) {
                    // 如果mapFragment为空，则创建一个并添加到界面上
                    groupFragment = new GroupFragment();
                    transaction.add(R.id.content, groupFragment);
                } else {
                    // 如果mapFragment不为空，则直接将它显示出来
                    transaction.show(groupFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        homeImage.setImageResource(R.drawable.icon);
        homeText.setTextColor(Color.parseColor("#82858b"));
        groupImage.setImageResource(R.drawable.icon);
        groupText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (groupFragment != null) {
            transaction.hide(groupFragment);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
