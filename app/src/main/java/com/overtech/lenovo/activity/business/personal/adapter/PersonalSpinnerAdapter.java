package com.overtech.lenovo.activity.business.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.person.Person;

import java.util.List;

/**
 * Created by Overtech on 16/5/12.
 */
public class PersonalSpinnerAdapter extends BaseAdapter {
    private Context ctx;
    private List<Person.Type> data;
    private String isDefault;//记录默认的是什么值

    public PersonalSpinnerAdapter(Context ctx, List<Person.Type> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position)._id;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_spinner_personal, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tv_spinner_item);
        textView.setText(data.get(position).name);
        if (data.get(position).isDefault.equals("1")) {
            isDefault = data.get(position).name;
        }

        return convertView;
    }

    public String getDefault() {
        return isDefault;
    }
}
