package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.os.Bundle;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.config.Debug;

public class DetailInformationFragment extends BaseFragment {

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_detail_information;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Debug.log("DetailInformationFragment==", "onStart");
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Debug.log("DetailInformationFragment==", "onResume");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Debug.log("DetailInformationFragment==", "onPause");
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Debug.log("DetailInformationFragment==", "onStop");
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Debug.log("DetailInformationFragment==", "onDestroy");
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Debug.log("DetailInformationFragment==", isVisibleToUser+"");
		if(isVisibleToUser){
			//开始网络加载
		}else{
			//取消加载
		}
	}

}
