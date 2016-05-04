package com.overtech.lenovo.activity.business.information.adapter;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.widget.bitmap.ImageLoader;

import java.util.List;

public class InformationItemAdapter extends BaseAdapter {
	private Context ctx;
	private List<Information.UserImg> data;

	public InformationItemAdapter(Context ctx,List<Information.UserImg> data) {
		this.ctx = ctx;
		this.data = data;
	}

	@Override
	public int getCount() {
//		return data==null?0 :(data.size()>9?9:data.size());// 限制每个item的最大加载图片数量为9，暂定
		if(data==null){
			return 0;
		}else{
			if(data.size()>9){
				return 9;
			}else{
				return data.size();
			}
		}
	}

	@Override
	public Object getItem(int position) {
		return data.get(position).img;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(ctx, R.layout.item_gridview_informationitem, null);
		}
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_picture_item);
		ImageLoader.getInstance().displayImage(data.get(position).img, imageView, R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Config.RGB_565);
		return convertView;
	}

	public void setUrls(List<Information.UserImg> data) {
		this.data = data;
	}

}
