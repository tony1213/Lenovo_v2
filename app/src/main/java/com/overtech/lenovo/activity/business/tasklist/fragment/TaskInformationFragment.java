package com.overtech.lenovo.activity.business.tasklist.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.tasklist.adapter.TaskInfoFragAdapter;
import com.overtech.lenovo.config.Debug;
import com.overtech.lenovo.entity.tasklist.TaskProcess;

import java.util.ArrayList;
import java.util.List;

public class TaskInformationFragment extends BaseFragment {
	private ListView mTaskProcess;
	private TaskInfoFragAdapter adapter;
	private List<TaskProcess> datas;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x1:
				stopProgress();
				Debug.log("taskinformationfragment", "收到handler发送的消息了");
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_task_information;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mTaskProcess = (ListView) mRootView.findViewById(R.id.lv_task_process);
		datas = new ArrayList<TaskProcess>();
		datas.add(new TaskProcess("开单", "2016/01/22", "单号：20160122-0008", ""));
		datas.add(new TaskProcess("接单", "", "", "接单"));
		datas.add(new TaskProcess("预约", "2016/01/22 10:50", "", "改约"));
		datas.add(new TaskProcess("到场", "2016/01/22 11:30", "", "到场"));
		datas.add(new TaskProcess("完成", "2016/01/22 12:00", "", "解决方案"));
		datas.add(new TaskProcess("评价", "2016/01/22 14:00", "问题已经解决，态度认真负责", ""));
		adapter = new TaskInfoFragAdapter(getActivity(), datas);
		mTaskProcess.setAdapter(adapter);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Debug.log("TaskInformationFragment==", "onStart");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Debug.log("TaskInformationFragment==", "onResume");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Debug.log("TaskInformationFragment==", "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Debug.log("TaskInformationFragment==", "onStop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Debug.log("TaskInformationFragment==", "onDestroy");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		Debug.log("TaskInformationFragment==", isVisibleToUser + "");
		if (isVisibleToUser) {
			startProgress("加载中...");
			handler.sendEmptyMessageDelayed(0x1, 5000);
			// 开始网络加载
		} else {
			// 取消网络加载
		}
	}

}
