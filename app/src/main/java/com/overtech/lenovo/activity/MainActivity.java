package com.overtech.lenovo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.fragment.InformationFragment;
import com.overtech.lenovo.activity.fragment.KnowledgeFragment;
import com.overtech.lenovo.activity.fragment.PersonalFragment;
import com.overtech.lenovo.activity.fragment.TaskListFragment;
import com.overtech.lenovo.activity.fragment.callback.FragmentCallback;
import com.overtech.lenovo.utils.FragmentUtils;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.tabview.TabView;
import com.overtech.lenovo.widget.tabview.TabView.OnTabChangeListener;

public class MainActivity extends BaseActivity implements OnTabChangeListener, FragmentCallback {
    private Toolbar toolbar;
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    public TaskListFragment taskListFragment;
    public KnowledgeFragment knowledgeFragment;
    public InformationFragment informationFragment;
    public PersonalFragment personalFragment;
    private TabView mTabView;
    private int mPreviousTabIndex = 0;//上一次的状态
    private int mCurrentTabIndex = 0;//当前状态
    private String uid;
    private long exitTime = 0;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        StackManager.getStackManager().pushActivity(this);
        TextView title = (TextView) findViewById(R.id.tv_toolbar_title);
        title.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mFragmentManager = getSupportFragmentManager();
        uid = (String) SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "");
        mCurrentTabIndex = 0;
        mPreviousTabIndex = -1;
        setupViews();
    }

    private void setupViews() {
        mTabView = (TabView) findViewById(R.id.view_tab);
        mTabView.setOnTabChangeListener(this);
        mTabView.setCurrentTab(mCurrentTabIndex);
    }

    @Override
    public void onFragmentCallback(Fragment fragment, int id, Bundle args) {
        mTabView.setCurrentTab(0);
    }

    @Override
    public void onTabChange(String tag) {

        if (tag != null) {
            if (tag.equals("tasklist")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 0;
                replaceFragment(TaskListFragment.class);
            } else if ("knowledge".equals(tag)) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 1;
                replaceFragment(KnowledgeFragment.class);
            } else if (tag.equals("information")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 2;
                replaceFragment(InformationFragment.class);
            } else if (tag.equals("personal")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 3;
                replaceFragment(PersonalFragment.class);
            }
        }
    }

    private void replaceFragment(Class<? extends Fragment> newFragment) {
        mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager, R.id.layout_content, mCurrentFragment, newFragment, null, false);
        if (newFragment.getSimpleName().equals(TaskListFragment.class.getSimpleName())) {
            taskListFragment = (TaskListFragment) mCurrentFragment;
        } else if (newFragment.getSimpleName().equals(KnowledgeFragment.class.getSimpleName())) {
            knowledgeFragment = (KnowledgeFragment) mCurrentFragment;
        } else if (newFragment.getSimpleName().equals(InformationFragment.class.getSimpleName())) {
            informationFragment = (InformationFragment) mCurrentFragment;
        } else {
            personalFragment = (PersonalFragment) mCurrentFragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mCurrentFragment instanceof InformationFragment) {
            if (informationFragment.llCommentUpContainer.getVisibility() == View.VISIBLE) {
                informationFragment.llCommentUpContainer.setVisibility(View.GONE);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Utilities.showToast("再按一次退出程序", this);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                StackManager.getStackManager().popAllActivitys();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getUid() {
        return uid;
    }
}
