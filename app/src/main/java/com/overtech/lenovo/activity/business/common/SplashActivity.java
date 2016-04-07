package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.ViewPagerAdapter;

import java.util.ArrayList;

/*
*
*App引导界面
*
*/
public class SplashActivity extends BaseActivity implements View.OnClickListener, OnPageChangeListener {
    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    private Button mBtnGoMain;
    // 引导图片资源
    private static final int[] pics = {R.mipmap.splash_1, R.mipmap.splash_2, R.mipmap.splash_3, R.mipmap.splash_4};

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mBtnGoMain = (Button) findViewById(R.id.btn_splash_next);

        views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vpAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(this);
        mBtnGoMain.setOnClickListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Logger.e("position:" + position + "====>" + "positionOffset:" + positionOffset + "=====>" + "positionOffsetPixels:" + positionOffsetPixels);
        if (position == pics.length - 1) {
            mBtnGoMain.setVisibility(View.VISIBLE);
        } else {
            mBtnGoMain.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_splash_next:
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}