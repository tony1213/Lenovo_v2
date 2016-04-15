package com.overtech.lenovo.activity.business.tasklist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

public class TaskDetailActivity extends BaseActivity {
    private TabLayout mTabLayout;
    // private ViewPager mViewPager;
    private CustomeViewPager mViewPager;
    private TextView mTitle;
    private TaskDetailAdapter adapter;
    private List<Fragment> listFragment;
    private List<String> listTitle;
    public TaskInformationFragment taskInfoFrag;// 本单信息
    private DetailInformationFragment detailInfoFrag;// 详细信息
    private StoreInformationFragment storeInfoFrag;// 门店信息
    private PropertyFragment propertyFrag;// 资产
    private String workorderCode;//工单号

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Bundle arg = getIntent().getExtras();
        workorderCode = arg.getString("workorder_code");

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (CustomeViewPager) findViewById(R.id.viewPager);
        mTitle = (TextView) findViewById(R.id.tv_task_detail_title);

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

        mTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String getWorkorderCode() {
        return workorderCode;
    }
}
