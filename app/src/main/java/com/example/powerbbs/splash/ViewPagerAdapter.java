package com.example.powerbbs.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.powerbbs.MainActivity;
import com.example.powerbbs.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 晞余 on 2018/2/26.
 */

public class ViewPagerAdapter  extends PagerAdapter{

        // 界面列表
        private List<View> views;
        private Activity activity;
        public ViewPagerAdapter(List<View> views, Activity activity) {
            this.views = views;
            this.activity = activity;
        }
        // 销毁arg1位置的界面
        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }
        @Override
        public void finishUpdate(ViewGroup arg0) {
        }
        // 获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }
        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(ViewGroup container, int arg1) {
            container.addView(views.get(arg1), 0);
            if (arg1 == views.size() - 1) {
                ImageView mStartWeiboImageButton = (ImageView) container
                        .findViewById(R.id.guideToStart);
                mStartWeiboImageButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 设置已经引导
                        setGuided();
                        goHome();

                    }

                });
            }
            return views.get(arg1);
        }

        private void goHome() {
            // 跳转
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }

        // method desc：设置已经引导过了，下次启动不用再次引导
        private void setGuided() {
            SharedPreferences preferences = activity.getSharedPreferences(activity.getResources().getString(R.string.FirstLauch), MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            // 存入数据
            editor.putBoolean(activity.getResources().getString(R.string.FirstLauch), false);
            editor.commit();
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {

        }
}
