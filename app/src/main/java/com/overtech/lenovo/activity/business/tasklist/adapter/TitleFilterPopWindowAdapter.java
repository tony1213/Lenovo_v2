package com.overtech.lenovo.activity.business.tasklist.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.overtech.lenovo.R;

/**
 * Created by Tony1213 on 16/3/23.
 * 工单页面顶部筛选
 */
public class TitleFilterPopWindowAdapter extends BaseAdapter {
    private Context context;

    private List<String> list;


    public TitleFilterPopWindowAdapter(Context context, List<String> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()==0?0:list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView==null) {
            convertView=LayoutInflater.from(context).inflate(R.layout.item_task_list_pop, null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.groupItem.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView groupItem;
    }

}
