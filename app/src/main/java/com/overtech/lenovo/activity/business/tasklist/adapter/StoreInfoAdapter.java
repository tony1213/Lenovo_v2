package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.widget.bitmap.ImageLoader;

public class StoreInfoAdapter extends Adapter<StoreInfoAdapter.MyViewHolder> {

	private Context ctx;
	private List<String> datas;

	public StoreInfoAdapter(Context ctx, List<String> datas) {
		this.ctx = ctx;
		this.datas = datas;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder vh, int position) {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().displayImage(datas.get(position), vh.imageView,
				R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Config.RGB_565);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		// TODO Auto-generated method stub
		MyViewHolder mVH = new MyViewHolder(LayoutInflater.from(ctx).inflate(
				R.layout.item_recyclerview_storeinfo, null));
		return mVH;
	}

	class MyViewHolder extends ViewHolder {
		ImageView imageView;

		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
			imageView = (ImageView) view
					.findViewById(R.id.iv_store_information);
		}
	}
}
