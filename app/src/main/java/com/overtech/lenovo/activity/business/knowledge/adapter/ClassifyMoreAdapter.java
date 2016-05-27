package com.overtech.lenovo.activity.business.knowledge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.entity.knowledge.Knowledges;

import java.util.List;

public class ClassifyMoreAdapter extends BaseAdapter {

    private Context context;
    private List<Knowledges.Knowledge> list;
    private int position = 0;
    Holder hold;

    public ClassifyMoreAdapter(Context context, List<Knowledges.Knowledge> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<Knowledges.Knowledge> list) {
        this.list = list;
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return list.get(position).knowledge_id;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int arg0, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_classify_morelist, null);
            hold = new Holder(view);
            view.setTag(hold);
        } else {
            hold = (Holder) view.getTag();
        }
        hold.txt.setText(list.get(arg0).subject);
        hold.txt.setTextColor(0xaaFF8C00);
        return view;
    }


    private static class Holder {
        TextView txt;

        public Holder(View view) {
            txt = (TextView) view.findViewById(R.id.moreitem_txt);
        }
    }
}
