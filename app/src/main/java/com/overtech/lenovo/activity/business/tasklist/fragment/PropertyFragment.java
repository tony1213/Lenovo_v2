package com.overtech.lenovo.activity.business.tasklist.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseFragment;
import com.overtech.lenovo.activity.business.tasklist.adapter.PropertyAdapter;
import com.overtech.lenovo.entity.tasklist.PropertyInfo;

public class PropertyFragment extends BaseFragment {
	private View convertView;
	private ListView mProperty;
	private String[][] infos = new String[][] {
			{ "类型", "品牌", "型号", "数量", "序列号", "报修截止日期", "备注" },
			{ "PC", "联想", "1730595", "2", "123456", "2016-10-10", "断网" },
			{ "POS", "惠普", "lied210", "1", "908098", "2016-09-09", "破损" },
			{ "IPAD", "华硕", "v12347", "3", "535345", "2016-09-10", "破损" },
			{ "路由器", "三星", "r234", "2", "23542", "2016-08-08", "挂掉" } };
	private List<PropertyInfo> datas;
	private PropertyAdapter adapter;
	private View header;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		convertView=inflater.inflate(R.layout.fragment_property, container, false);
		init();
		return convertView;
	}

	protected void init() {
		mProperty=(ListView) convertView.findViewById(R.id.lv_property);
		
		datas=new ArrayList<PropertyInfo>();
		PropertyInfo pi1=new PropertyInfo("PC", "联想", "1730595", "2", "123456", "2016-10-10", "断网");
		PropertyInfo pi2=new PropertyInfo("POS", "惠普", "lied210", "1", "908098", "2016-09-09", "破损");
		PropertyInfo pi3=new PropertyInfo("IPAD", "华硕", "v12347", "3", "535345", "2016-09-10", "破损");
		PropertyInfo pi4=new PropertyInfo("路由器", "三星", "r234", "2", "23542", "2016-08-08", "挂掉");
		datas.add(pi1);
		datas.add(pi2);
		datas.add(pi3);
		datas.add(pi4);
		adapter=new PropertyAdapter(getActivity(), datas);
		
		header=LayoutInflater.from(getActivity()).inflate(R.layout.item_headerview_property, null);
		mProperty.addHeaderView(header);
		mProperty.setAdapter(adapter);
	}

}
