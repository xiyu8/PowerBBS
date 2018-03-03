package com.example.powerbbs.splash;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.powerbbs.R;
import com.example.powerbbs.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViews();
        initDots();
    }

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.guide_pagers1, null));
        views.add(inflater.inflate(R.layout.guide_pagers2, null));
        views.add(inflater.inflate(R.layout.guide_pagers3, null));
        views.add(inflater.inflate(R.layout.guide_pagers4, null));
        // 初始化Adapter
        viewPagerAdapter = new ViewPagerAdapter(views, this);

        viewPager = findViewById(R.id.guideViewpager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;
    private void initDots() {
        LinearLayout ll = findViewById(R.id.ll);
        dots = new ImageView[views.size()];
        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }
    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurrentDot(arg0);
    }
}
