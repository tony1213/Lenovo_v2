package com.overtech.lenovo.activity.business.information.adapter;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.information.Information;
import com.overtech.lenovo.widget.bitmap.ImageLoader;

import java.util.List;

public class InformationItemAdapter extends BaseAdapter {
    private Context ctx;
    private List<Information.UserImg> data;

    public InformationItemAdapter(Context ctx, List<Information.UserImg> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
//		return data==null?0 :(data.size()>9?9:data.size());// 限制每个item的最大加载图片数量为9，暂定
        if (data == null) {
            Logger.e("InformationItemAdapter====getCount()===" + 0);
            return 0;
        } else {
            if (data.size() > 9) {
                Logger.e("InformationItemAdapter====getCount()===" + 9);
                return 9;
            } else {
                Logger.e("InformationItemAdapter====getCount()===" + data.size());
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
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_gridview_informationitem, parent, false);
            vh=new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        Logger.e("InformationItemAdapter===getView()===" + position);
        ImageLoader.getInstance().displayImage(data.get(position).img, vh.imageView, R.mipmap.icon_common_default_stub, R.mipmap.icon_common_default_error, Config.RGB_565);
        return convertView;
    }

    public void setUrls(List<Information.UserImg> data) {
        this.data = data;
    }

    public List<Information.UserImg> getUrls() {
        return data;
    }
    class ViewHolder {
        ImageView imageView;
        public ViewHolder(View view){
            imageView= (ImageView) view.findViewById(R.id.iv_picture_item);
        }
    }
}
