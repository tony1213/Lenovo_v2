package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TaskDetailAdapter extends FragmentPagerAdapter {
	private List<Fragment> listFragment;
	private List<String> listTitle;

	public TaskDetailAdapter(FragmentManager fm, List<Fragment> listFragment,
			List<String> listTitle) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.listFragment = listFragment;
		this.listTitle = listTitle;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return listFragment.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listFragment.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return listTitle.get(position % listTitle.size());
	}

}
