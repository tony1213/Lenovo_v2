package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.TaskProcess;

public class TaskInfoFragAdapter extends BaseAdapter {
	private Context ctx;
	private List<TaskProcess> datas;

	public TaskInfoFragAdapter(Context ctx, List<TaskProcess> datas) {
		this.ctx = ctx;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(ctx,
					R.layout.item_listview_task_process, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		TaskProcess task = datas.get(position);
		vh.mTaskState.setText(task.getState());
		vh.mTaskTime.setText(task.getTime());
		if (task.getAction().equals("")) {
			vh.mButton.setVisibility(View.GONE);
		} else {
			vh.mButton.setVisibility(View.VISIBLE);
			vh.mButton.setText(task.getAction());
		}
		if (task.getOther().equals("")) {
			vh.mOther.setVisibility(View.GONE);
		} else {
			vh.mOther.setVisibility(View.VISIBLE);
			vh.mOther.setText(task.getOther());
		}

		return convertView;
	}

	class ViewHolder {
		TextView mTaskState;
		TextView mTaskTime;
		TextView mOther;
		AppCompatButton mButton;

		public ViewHolder(View view) {
			mTaskState = (TextView) view
					.findViewById(R.id.tv_task_process_state);
			mTaskTime = (TextView) view.findViewById(R.id.tv_task_process_time);
			mOther = (TextView) view.findViewById(R.id.tv_task_process_other);
			mButton = (AppCompatButton) view.findViewById(R.id.bt_task_action);
		}
	}

}
