package com.overtech.lenovo.activity.business.tasklist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskDetailAdapter;
import com.overtech.lenovo.activity.business.tasklist.customeviewpage.CustomeViewPager;
import com.overtech.lenovo.activity.business.tasklist.fragment.DetailInformationFragment;
import com.overtech.lenovo.activity.business.tasklist.fragment.PropertyFragment;
import com.overtech.lenovo.activity.business.tasklist.fragment.StoreInformationFragment;
import com.overtech.lenovo.activity.business.tasklist.fragment.TaskInformationFragment;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.utils.StackManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends BaseActivity {
    private Toolbar toolbar;
    private TabLayout mTabLayout;
    // private ViewPager mViewPager;
    private CustomeViewPager mViewPager;
    private TaskDetailAdapter adapter;
    private List<Fragment> listFragment;
    private List<String> listTitle;
    public TaskInformationFragment taskInfoFrag;// 本单信息
    private DetailInformationFragment detailInfoFrag;// 详细信息
    public StoreInformationFragment storeInfoFrag;// 门店信息
    private PropertyFragment propertyFrag;// 资产
    private String workorderCode;//工单号

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        StackManager.getStackManager().pushActivity(this);
        workorderCode = getIntent().getStringExtra("workorder_code");
        Logger.e("workorderCode" + workorderCode);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (CustomeViewPager) findViewById(R.id.viewPager);

        toolbar.setTitle("本单详情");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        taskInfoFrag = new TaskInformationFragment();
        detailInfoFrag = new DetailInformationFragment();
        storeInfoFrag = new StoreInformationFragment();
        propertyFrag = new PropertyFragment();

        listFragment = new ArrayList<Fragment>();
        listFragment.add(taskInfoFrag);
        listFragment.add(detailInfoFrag);
        listFragment.add(storeInfoFrag);
        listFragment.add(propertyFrag);

        listTitle = new ArrayList<String>();
        listTitle.add("本单信息");
        listTitle.add("详细信息");
        listTitle.add("门店信息");
        listTitle.add("资产");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);// 设置TabLayout的模式
        mTabLayout.addTab(mTabLayout.newTab().setText(listTitle.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(listTitle.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(listTitle.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(listTitle.get(3)));

        adapter = new TaskDetailAdapter(getSupportFragmentManager(),
                listFragment, listTitle);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public String getWorkorderCode() {
        return workorderCode;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StackManager.getStackManager().popActivity(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
