package com.overtech.lenovo.activity.business.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.overtech.lenovo.activity.business.tasklist.adapter.ViewPagerAdapter;
import java.util.ArrayList;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import com.overtech.lenovo.R;
import com.overtech.lenovo.debug.Logger;

/*
*
*App引导界面
*
*/
public class SplashActivity extends Activity implements View.OnClickListener,OnPageChangeListener {
    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    private  Button mBtnGoMain;
    // 引导图片资源
    private static final int[] pics = {R.mipmap.splash_1, R.mipmap.splash_2, R.mipmap.splash_3, R.mipmap.splash_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);
        mBtnGoMain=(Button)findViewById(R.id.btn_splash_next);
        mBtnGoMain.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            //防止图片不能填满屏幕
            iv.setScaleType(ScaleType.FIT_XY);
            //加载图片资源
            iv.setImageResource(pics[i]);
            views.add(iv);
        }

        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Logger.e("position:" + position + "====>" + "positionOffset:" + positionOffset + "=====>" + "positionOffsetPixels:" + positionOffsetPixels);
        if (position==pics.length-1){
            mBtnGoMain.setVisibility(View.VISIBLE);
        }else {
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
        switch (v.getId()){
            case R.id.btn_splash_next:
                Intent intent=new Intent();
                intent.setClass(this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}