package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.PropertyInfo;

public class PropertyAdapter extends BaseAdapter {
	private List<PropertyInfo> datas;
	private Context ctx;

	public PropertyAdapter(Context ctx, List<PropertyInfo> datas) {
		this.ctx = ctx;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		PropertyInfo data = datas.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(
					R.layout.item_listview_property, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (position % 2 == 0) {
			vh.parent.setBackgroundResource(R.color.main_white);
		} else {
			vh.parent.setBackgroundResource(R.color.colo_grey);
		}
		vh.calssify.setText(data.getClassify());
		vh.brand.setText(data.getBrand());
		vh.model.setText(data.getModel());
		vh.model.setText(data.getModel());
		vh.account.setText(data.getAccount());
		vh.serial.setText(data.getSerial());
		vh.finalDate.setText(data.getFinalDate());
		vh.remark.setText(data.getRemark());
		return convertView;
	}

	class ViewHolder {
		LinearLayout parent;
		TextView calssify;// 分类
		TextView brand;// 品牌
		TextView model;// 型号
		TextView account;// 数量
		TextView serial;// 序列号
		TextView finalDate;// 报修截止日期
		TextView remark;// 备注

		public ViewHolder(View convertView) {
			parent = (LinearLayout) convertView
					.findViewById(R.id.ll_property_item);
			calssify = (TextView) convertView.findViewById(R.id.tv_classify);
			brand = (TextView) convertView.findViewById(R.id.tv_brand);
			model = (TextView) convertView.findViewById(R.id.tv_model);
			account = (TextView) convertView.findViewById(R.id.tv_account);
			serial = (TextView) convertView.findViewById(R.id.tv_serial);
			finalDate = (TextView) convertView.findViewById(R.id.tv_final_date);
			remark = (TextView) convertView.findViewById(R.id.tv_remark);
		}
	}

}
