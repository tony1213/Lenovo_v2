package com.overtech.lenovo.activity.business.tasklist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.tasklist.PropertyInfo;

import java.util.List;

public class PropertyAdapter extends BaseAdapter {
    private List<PropertyInfo.Property> datas;
    private Context ctx;

    public PropertyAdapter(Context ctx, List<PropertyInfo.Property> datas) {
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
        PropertyInfo.Property data = datas.get(position);
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
        vh.assert_type.setText(data.assert_type);
        vh.brand.setText(data.brand);
        vh.model.setText(data.model);
        vh.quantity.setText(data.quantity);
        vh.identification_code.setText(data.identification_code);
        vh.code.setText(data.code);
        vh.repair_closing_date.setText(data.repair_closing_date);
        vh.remarks.setText(data.remarks);
        return convertView;
    }

    class ViewHolder {
        LinearLayout parent;
        TextView assert_type;// 分类
        TextView brand;// 品牌
        TextView model;// 型号
        TextView quantity;// 数量
        TextView identification_code;// 序列号
        TextView code;//资产编号
        TextView repair_closing_date;// 报修截止日期
        TextView remarks;// 备注

        public ViewHolder(View convertView) {
            parent = (LinearLayout) convertView
                    .findViewById(R.id.ll_property_item);
            assert_type = (TextView) convertView.findViewById(R.id.tv_classify);
            brand = (TextView) convertView.findViewById(R.id.tv_brand);
            model = (TextView) convertView.findViewById(R.id.tv_model);
            quantity = (TextView) convertView.findViewById(R.id.tv_account);
            identification_code = (TextView) convertView.findViewById(R.id.tv_serial);
            code = (TextView) convertView.findViewById(R.id.tv_code);
            repair_closing_date = (TextView) convertView.findViewById(R.id.tv_final_date);
            remarks = (TextView) convertView.findViewById(R.id.tv_remark);
        }
    }

}
