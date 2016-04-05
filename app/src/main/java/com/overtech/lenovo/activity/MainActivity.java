package com.overtech.lenovo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.fragment.callback.FragmentCallback;
import com.overtech.lenovo.activity.fragment.InformationFragment;
import com.overtech.lenovo.activity.fragment.KnowledgeFragment;
import com.overtech.lenovo.activity.fragment.PersonalFragment;
import com.overtech.lenovo.activity.fragment.TaskListFragment;
import com.overtech.lenovo.utils.FragmentUtils;
import com.overtech.lenovo.widget.tabview.TabView;
import com.overtech.lenovo.widget.tabview.TabView.OnTabChangeListener;

public class MainActivity extends BaseActivity implements OnTabChangeListener,FragmentCallback {

	private FragmentManager mFragmentManager;
	private Fragment mCurrentFragment;
	private TabView mTabView;
	private int mPreviousTabIndex = 0;//上一次的状态
	private int mCurrentTabIndex = 0;//当前状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFragmentManager = getSupportFragmentManager();
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
	}
}
